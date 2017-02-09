package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Weapon;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BasePlayerStateTest {

  @Mock
  private MapState mapState;
  @Mock
  private StateFactory stateFactory;
  @Mock
  private EventBus eventBus;
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
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  private BasePlayerState state;

  @Before
  public void setUp() {
    state = new BasePlayerState(eventBus, mapState, stateFactory, player);
  }

  @Test
  public void enter() {
    state.enter();

    verify(eventBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getAllValues().get(0)).isSameAs(state);
  }

  @Test
  public void exit() {
    state.exit();

    verify(eventBus).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, state, ExitState.class);
  }

  @Test
  public void go_to() {
    state.goTo(state2);

    verify(mapState).goTo(state2);
  }

  @Test
  public void back() {
    state.back();

    verify(mapState).back();
  }

  @Test
  public void roll_back() {
    state.rollback();

    verify(mapState).rollback();
  }

  @Test
  public void branch_to() {
    state.branchTo(waiting);

    verify(mapState).branchTo(waiting);
  }

  @Test
  public void pop() {
    state.popLast();

    verify(mapState).popLast();
  }

  @Test
  public void get_player() {
    assertThat(state.getPlayer()).isSameAs(player);
  }

  @Test
  public void finish() {
    when(stateFactory.createWaiting()).thenReturn(waiting);

    state.finish();

    verify(player).setActionable(false);
    verify(mapState).branchTo(waiting);
  }

  @Test
  public void actions() {
    when(player.fluentItems()).thenReturn(FluentIterable.of(weapon, consumable));

    ImmutableList<Action> actions = state.getActions();

    assertThat(actions).hasSize(4);
    assertThat(actions.get(0)).isInstanceOf(SelectWeaponAction.class);
    assertThat(actions.get(1)).isInstanceOf(UseConsumableAction.class);
    assertThat(actions.get(2)).isInstanceOf(FinishAction.class);
    assertThat(actions.get(3)).isInstanceOf(BackAction.class);
  }
}