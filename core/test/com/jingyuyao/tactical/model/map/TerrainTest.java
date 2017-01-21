package com.jingyuyao.tactical.model.map;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.common.Algorithms;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.map.Terrain.Type;
import com.jingyuyao.tactical.model.state.MapState;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TerrainTest {

  private static final Coordinate COORDINATE = new Coordinate(10, 10);
  private static final Type TYPE = Type.NORMAL;

  @Mock
  private EventBus eventBus;
  @Mock
  private MapState mapState;
  @Mock
  private Character character;

  private Terrain terrain;

  @Before
  public void setUp() {
    terrain = new Terrain(eventBus, COORDINATE, TYPE);
  }

  @Test
  public void select() {
    terrain.select(mapState);

    verify(mapState).select(terrain);
  }

  @Test
  public void highlight() {
    terrain.highlight(mapState);

    verify(mapState).highlight(terrain);
  }

  @Test
  public void getType() {
    assertThat(terrain.getType()).isEqualTo(TYPE);
  }

  @Test
  public void move_penalty_can_pass() {
    when(character.canPassTerrainType(TYPE)).thenReturn(true);

    // we are not going to test the actual values for not
    assertThat(terrain.getMovementPenalty(character)).isNotEqualTo(Algorithms.NO_EDGE);
  }

  @Test
  public void move_penalty_cannot_pass() {
    when(character.canPassTerrainType(TYPE)).thenReturn(false);

    assertThat(terrain.getMovementPenalty(character)).isEqualTo(Algorithms.NO_EDGE);
  }
}