package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.battle.Target;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Movements;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ControllingActionStateTest {

  @Mock
  private ModelBus modelBus;
  @Mock
  private WorldState worldState;
  @Mock
  private StateFactory stateFactory;
  @Mock
  private Movements movements;
  @Mock
  private Cell cell;
  @Mock
  private Ship ship;
  @Mock
  private Weapon weapon;
  @Mock
  private Consumable consumable;
  @Mock
  private ImmutableList<Target> targets;
  @Mock
  private SelectingTarget selectingTarget;
  @Mock
  private UsingConsumable usingConsumable;

  private ControllingActionState state;

  @Before
  public void setUp() {
    when(cell.ship()).thenReturn(Optional.of(ship));
    when(ship.isControllable()).thenReturn(true);
    state = new ControllingActionState(modelBus, worldState, stateFactory, movements, cell);
  }

  @Test
  public void get_player() {
    assertThat(state.getShip()).isSameAs(ship);
  }

  @Test
  public void select_weapon() {
    when(weapon.createTargets(movements, cell)).thenReturn(targets);
    when(stateFactory.createSelectingTarget(cell, weapon, targets))
        .thenReturn(selectingTarget);

    state.selectWeapon(weapon);

    verify(worldState).goTo(selectingTarget);
  }

  @Test
  public void select_consumable() {
    when(stateFactory.createUsingConsumable(cell, consumable)).thenReturn(usingConsumable);

    state.selectConsumable(consumable);

    verify(worldState).goTo(usingConsumable);
  }

  @Test
  public void actions() {
    when(ship.getWeapons()).thenReturn(ImmutableList.of(weapon));
    when(ship.getConsumables()).thenReturn(ImmutableList.of(consumable));

    ImmutableList<Action> actions = state.getActions();

    assertThat(actions).hasSize(4);
    assertThat(actions.get(0)).isInstanceOf(SelectWeaponAction.class);
    assertThat(actions.get(1)).isInstanceOf(SelectConsumableAction.class);
    assertThat(actions.get(2)).isInstanceOf(FinishAction.class);
    assertThat(actions.get(3)).isInstanceOf(BackAction.class);
  }
}