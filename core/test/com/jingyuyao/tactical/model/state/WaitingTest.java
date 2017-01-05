package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.map.Terrain;
import com.jingyuyao.tactical.model.mark.Markings;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WaitingTest {

  @Mock
  private EventBus eventBus;
  @Mock
  private MapState mapState;
  @Mock
  private Markings markings;
  @Mock
  private StateFactory stateFactory;
  @Mock
  private Player player;
  @Mock
  private Enemy enemy;
  @Mock
  private Terrain terrain;
  @Mock
  private Moving moving;

  private Waiting waiting;

  @Before
  public void setUp() {
    waiting = new Waiting(eventBus, mapState, markings, stateFactory);
  }

  @Test
  public void select_player_actionable() {
    when(player.isActionable()).thenReturn(true);
    when(stateFactory.createMoving(player)).thenReturn(moving);

    waiting.select(player);

    verify(stateFactory).createMoving(player);
    verify(mapState).push(moving);
  }

  @Test
  public void select_player_not_actionable() {
    when(player.isActionable()).thenReturn(false);

    waiting.select(player);

    verifyZeroInteractions(stateFactory);
    verifyZeroInteractions(mapState);
  }

  @Test
  public void select_enemy() {
    waiting.select(enemy);

    verify(markings).toggleDangerArea(enemy);
  }

  @Test
  public void select_terrain() {
    waiting.select(terrain);

    verify(mapState).pop();
  }

  @Test
  public void actions() {
    List<Action> actions = waiting.getActions();
    assertThat(actions).hasSize(1);
    Action endTurn = actions.get(0);

    endTurn.run();

    verify(eventBus).post(endTurn);
  }
}
