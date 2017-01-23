package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.map.Movement;
import com.jingyuyao.tactical.model.map.MovementFactory;
import com.jingyuyao.tactical.model.map.Terrain;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WaitingTest {

  @Mock
  private MapState mapState;
  @Mock
  private StateFactory stateFactory;
  @Mock
  private EventBus eventBus;
  @Mock
  private Player player;
  @Mock
  private Enemy enemy;
  @Mock
  private Terrain terrain;
  @Mock
  private Moving moving;
  @Mock
  private Retaliating retaliating;
  @Mock
  private MovementFactory movementFactory;
  @Mock
  private Movement movement;

  private Waiting waiting;

  @Before
  public void setUp() {
    waiting = new Waiting(mapState, stateFactory, eventBus, movementFactory);
  }

  @Test
  public void select_player_actionable() {
    when(player.isActionable()).thenReturn(true);
    when(movementFactory.create(player)).thenReturn(movement);
    when(stateFactory.createMoving(player, movement)).thenReturn(moving);

    waiting.select(player);

    verify(stateFactory).createMoving(player, movement);
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

    verify(mapState).pop();
  }

  @Test
  public void select_terrain() {
    waiting.select(terrain);

    verify(mapState).pop();
  }

  @Test
  public void actions_endturn() {
    when(stateFactory.createRetaliating()).thenReturn(retaliating);

    List<Action> actions = waiting.getActions();
    assertThat(actions).hasSize(1);
    Action endTurn = actions.get(0);

    endTurn.run();

    verify(eventBus).post(endTurn);
    verify(mapState).push(retaliating);
  }
}
