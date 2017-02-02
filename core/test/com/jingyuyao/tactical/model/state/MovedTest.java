package com.jingyuyao.tactical.model.state;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.map.Movement;
import com.jingyuyao.tactical.model.map.Movements;
import com.jingyuyao.tactical.model.terrain.Terrain;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MovedTest {

  @Mock
  private MapState mapState;
  @Mock
  private StateFactory stateFactory;
  @Mock
  private Movements movements;
  @Mock
  private Player player;
  @Mock
  private Player otherPlayer;
  @Mock
  private Enemy enemy;
  @Mock
  private Terrain terrain;
  @Mock
  private Movement movement;
  @Mock
  private Moving moving;

  private Moved moved;

  @Before
  public void setUp() {
    moved = new Moved(mapState, stateFactory, movements, player);
  }

  @Test
  public void select_player() {
    moved.select(player);

    verify(mapState).back();
    verifyNoMoreInteractions(mapState);
  }

  @Test
  public void select_other_player_not_actionable() {
    when(otherPlayer.isActionable()).thenReturn(false);

    moved.select(otherPlayer);

    verify(mapState).rollback();
    verifyNoMoreInteractions(mapState);
  }

  @Test
  public void select_other_player_actionable() {
    when(otherPlayer.isActionable()).thenReturn(true);
    when(movements.distanceFrom(otherPlayer)).thenReturn(movement);
    when(stateFactory.createMoving(otherPlayer, movement)).thenReturn(moving);

    moved.select(otherPlayer);

    verify(mapState).rollback();
    verify(mapState).goTo(moving);
    verifyNoMoreInteractions(mapState);
  }

  @Test
  public void select_enemy() {
    moved.select(enemy);

    verify(mapState).back();
    verifyNoMoreInteractions(mapState);
  }

  @Test
  public void select_terrain() {
    moved.select(terrain);

    verify(mapState).back();
    verifyNoMoreInteractions(mapState);
  }
}