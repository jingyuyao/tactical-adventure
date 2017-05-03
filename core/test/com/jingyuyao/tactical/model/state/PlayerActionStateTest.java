package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Movements;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PlayerActionStateTest {

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
  private Player player;
  @Mock
  private Waiting waiting;
  @Mock
  private Weapon weapon;
  @Mock
  private Consumable consumable;
  @Mock
  private State state2;
  @Mock
  private ImmutableList<Target> targets;
  @Mock
  private SelectingTarget selectingTarget;
  @Mock
  private UsingConsumable usingConsumable;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  private PlayerActionState state;

  @Before
  public void setUp() {
    when(cell.player()).thenReturn(Optional.of(player));
    state = new PlayerActionState(modelBus, worldState, stateFactory, movements, cell);
  }

  @Test(expected = NullPointerException.class)
  public void setUp_no_player() {
    when(cell.player()).thenReturn(Optional.<Player>absent());
    state = new PlayerActionState(modelBus, worldState, stateFactory, movements, cell);
  }

  @Test
  public void enter() {
    state.enter();

    verify(modelBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getAllValues().get(0)).isSameAs(state);
  }

  @Test
  public void exit() {
    state.exit();

    verify(modelBus).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, state, ExitState.class);
  }

  @Test
  public void go_to() {
    state.goTo(state2);

    verify(worldState).goTo(state2);
  }

  @Test
  public void back() {
    state.back();

    verify(worldState).back();
  }

  @Test
  public void roll_back() {
    state.rollback();

    verify(worldState).rollback();
  }

  @Test
  public void branch_to() {
    state.branchTo(waiting);

    verify(worldState).branchTo(waiting);
  }

  @Test
  public void remove_self() {
    state.removeSelf();

    verify(worldState).remove(state);
  }

  @Test
  public void get_player() {
    assertThat(state.getPlayer()).isSameAs(player);
  }

  @Test
  public void select_weapon() {
    when(weapon.createTargets(movements, cell)).thenReturn(targets);
    when(stateFactory.createSelectingTarget(player, weapon, targets)).thenReturn(selectingTarget);

    state.selectWeapon(weapon);

    verify(worldState).goTo(selectingTarget);
  }

  @Test
  public void select_consumable() {
    when(stateFactory.createUsingConsumable(player, consumable)).thenReturn(usingConsumable);

    state.selectConsumable(consumable);

    verify(worldState).goTo(usingConsumable);
  }

  @Test
  public void finish() {
    when(stateFactory.createWaiting()).thenReturn(waiting);

    state.finish();

    verify(player).setActionable(false);
    verify(worldState).branchTo(waiting);
  }

  @Test
  public void actions() {
    when(player.getWeapons()).thenReturn(ImmutableList.of(weapon));
    when(player.getConsumables()).thenReturn(ImmutableList.of(consumable));

    ImmutableList<Action> actions = state.getActions();

    assertThat(actions).hasSize(4);
    assertThat(actions.get(0)).isInstanceOf(SelectWeaponAction.class);
    assertThat(actions.get(1)).isInstanceOf(SelectConsumableAction.class);
    assertThat(actions.get(2)).isInstanceOf(FinishAction.class);
    assertThat(actions.get(3)).isInstanceOf(BackAction.class);
  }

}