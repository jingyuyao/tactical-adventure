package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.Cell;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SelectingTargetTest {

  @Mock
  private MapState mapState;
  @Mock
  private StateFactory stateFactory;
  @Mock
  private EventBus eventBus;
  @Mock
  private Cell cell;
  @Mock
  private Player player;
  @Mock
  private Weapon weapon;
  @Mock
  private Target target1;
  @Mock
  private Target target2;
  @Mock
  private Battling battling;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  private SelectingTarget selectingTarget;

  @Before
  public void setUp() {
    selectingTarget =
        new SelectingTarget(
            eventBus, mapState, stateFactory, player, weapon, ImmutableList.of(target1, target2));
  }

  @Test
  public void enter() {
    selectingTarget.enter();

    verify(eventBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getAllValues().get(0)).isSameAs(selectingTarget);
  }

  @Test
  public void exit() {
    selectingTarget.exit();

    verify(eventBus).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, selectingTarget, ExitState.class);
  }

  @Test
  public void select_target() {
    when(target1.selectedBy(cell)).thenReturn(true);
    when(stateFactory.createBattling(player, weapon, target1)).thenReturn(battling);

    selectingTarget.select(cell);

    verify(mapState).goTo(battling);
  }

  @Test
  public void select_no_select() {
    selectingTarget.select(cell);

    verifyZeroInteractions(mapState);
  }

  @Test
  public void actions() {
    ImmutableList<Action> actions = selectingTarget.getActions();

    assertThat(actions).hasSize(1);
    assertThat(actions.get(0)).isInstanceOf(BackAction.class);
  }
}