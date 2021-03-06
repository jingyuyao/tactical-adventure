package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.base.Optional;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.event.Promise;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Movement;
import com.jingyuyao.tactical.model.world.Path;
import com.jingyuyao.tactical.model.world.World;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MovingTest {

  @Mock
  private WorldState worldState;
  @Mock
  private StateFactory stateFactory;
  @Mock
  private World world;
  @Mock
  private ModelBus modelBus;
  @Mock
  private Cell cell;
  @Mock
  private Cell cell2;
  @Mock
  private Ship ship;
  @Mock
  private Ship otherShip;
  @Mock
  private Moved moved;
  @Mock
  private Movement movement;
  @Mock
  private Path path;
  @Mock
  private Transition transition;
  @Mock
  private Moving anotherMoving;
  @Mock
  private Movement otherMovement;
  @Mock
  private Weapon weapon;
  @Mock
  private Consumable consumable;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  private Moving moving;

  @Before
  public void setUp() {
    when(cell.ship()).thenReturn(Optional.of(ship));
    when(ship.isControllable()).thenReturn(true);
    moving = new Moving(modelBus, worldState, stateFactory, world, cell, movement);
  }

  @Test
  public void enter() {
    moving.enter();

    verify(modelBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getAllValues().get(0)).isSameAs(moving);
  }

  @Test
  public void canceled_nothing() {
    moving.canceled();

    verify(ship).isControllable();
    verifyZeroInteractions(ship);
  }

  @Test
  public void canceled_move() {
    select_can_move();

    moving.canceled();

    verify(world).moveShip(cell2, cell);
  }

  @Test
  public void exit() {
    moving.exit();

    verify(modelBus).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, moving, ExitState.class);
  }

  @Test
  public void select_same_ship() {
    when(cell2.ship()).thenReturn(Optional.of(ship));

    moving.select(cell2);

    verifyZeroInteractions(worldState);
  }

  @Test
  public void select_other_ship_not_controllable() {
    when(cell2.ship()).thenReturn(Optional.of(otherShip));
    when(otherShip.isControllable()).thenReturn(false);

    moving.select(cell2);

    verify(worldState).rollback();
    verifyNoMoreInteractions(worldState);
  }

  @Test
  public void select_other_ship_controllable() {
    when(cell2.ship()).thenReturn(Optional.of(otherShip));
    when(otherShip.isControllable()).thenReturn(true);
    when(world.getShipMovement(cell2)).thenReturn(otherMovement);
    when(stateFactory.createMoving(cell2, otherMovement)).thenReturn(anotherMoving);

    moving.select(cell2);

    verify(worldState).rollback();
    verify(worldState).goTo(anotherMoving);
    verifyNoMoreInteractions(worldState);
  }

  @Test
  public void select_can_move() {
    when(cell2.ship()).thenReturn(Optional.<Ship>absent());
    when(movement.getOrigin()).thenReturn(cell);
    when(movement.canMoveTo(cell2)).thenReturn(true);
    when(movement.pathTo(cell2)).thenReturn(path);
    when(stateFactory.createMoved(cell2)).thenReturn(moved);
    when(stateFactory.createTransition()).thenReturn(transition);
    when(world.moveShip(path)).thenReturn(Promise.immediate());

    moving.select(cell2);

    InOrder inOrder = Mockito.inOrder(ship, world, worldState);
    inOrder.verify(worldState).goTo(transition);
    inOrder.verify(world).moveShip(path);
    inOrder.verify(worldState).goTo(moved);
    verifyNoMoreInteractions(worldState);
  }

  @Test
  public void select_cannot_move() {
    when(cell2.ship()).thenReturn(Optional.<Ship>absent());
    when(movement.canMoveTo(cell2)).thenReturn(false);

    moving.select(cell2);

    verifyZeroInteractions(worldState);
  }

  @Test
  public void actions() {
    when(ship.getWeapons()).thenReturn(Collections.singletonList(weapon));
    when(ship.getConsumables()).thenReturn(Collections.singletonList(consumable));

    List<Action> actions = moving.getActions();

    assertThat(actions).hasSize(4);
    assertThat(actions.get(0)).isInstanceOf(SelectWeaponAction.class);
    assertThat(actions.get(1)).isInstanceOf(SelectConsumableAction.class);
    assertThat(actions.get(2)).isInstanceOf(FinishAction.class);
    assertThat(actions.get(3)).isInstanceOf(BackAction.class);
  }
}
