package com.jingyuyao.tactical.model.map;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;

import com.google.common.collect.Multiset;
import com.jingyuyao.tactical.model.character.Character;
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
  private Multiset<Marker> markers;
  @Mock
  private MapState mapState;
  @Mock
  private Character character;

  private Terrain terrain;

  @Before
  public void setUp() {
    terrain = new Terrain(COORDINATE, markers, TYPE);
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
}