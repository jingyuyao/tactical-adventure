package com.jingyuyao.tactical.model.terrain;

import static org.mockito.Mockito.verify;

import com.google.common.collect.Multiset;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.map.MapObjectData;
import com.jingyuyao.tactical.model.map.Marker;
import com.jingyuyao.tactical.model.state.MapState;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AbstractTerrainTest {

  @Mock
  private MapObjectData data;
  @Mock
  private Multiset<Marker> markers;
  @Mock
  private MapState mapState;

  private Terrain terrain;

  @Before
  public void setUp() {
    terrain = new TerrainImpl(data, markers);
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

  private static class TerrainImpl extends AbstractTerrain {

    TerrainImpl(MapObjectData data, Multiset<Marker> markers) {
      super(data, markers);
    }

    @Override
    public boolean canHold(Character character) {
      return false;
    }

    @Override
    public int getMovementPenalty() {
      return 0;
    }
  }
}