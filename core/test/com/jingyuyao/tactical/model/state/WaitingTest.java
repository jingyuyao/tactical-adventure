package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.graph.Graph;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.map.Characters;
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
  private Characters characters;
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
  @Mock
  private Graph<Coordinate> moveGraph;

  private Waiting waiting;

  @Before
  public void setUp() {
    waiting = new Waiting(mapState, stateFactory, movementFactory, characters);
  }

  @Test
  public void select_player_actionable() {
    when(player.isActionable()).thenReturn(true);
    when(player.createMoveGraph()).thenReturn(moveGraph);
    when(movementFactory.create(moveGraph)).thenReturn(movement);
    when(stateFactory.createMoving(player, movement)).thenReturn(moving);

    waiting.select(player);

    verify(stateFactory).createMoving(player, movement);
    verify(mapState).goTo(moving);
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

    verify(mapState).back();
  }

  @Test
  public void select_terrain() {
    waiting.select(terrain);

    verify(mapState).back();
  }

  @Test
  public void actions_endturn() {
    when(characters.iterator()).thenReturn(ImmutableList.of(player, enemy).iterator());
    when(stateFactory.createRetaliating()).thenReturn(retaliating);

    List<Action> actions = waiting.getActions();
    assertThat(actions).hasSize(1);
    Action endTurn = actions.get(0);

    endTurn.run();

    verify(player).setActionable(true);
    verify(mapState).goTo(retaliating);
  }
}
