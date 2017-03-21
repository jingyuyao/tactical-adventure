package com.jingyuyao.tactical.model.terrain;

import static org.mockito.Mockito.verify;

import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.state.SelectionHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AbstractTerrainTest {

  private static final Coordinate COORDINATE = new Coordinate(0, 0);

  @Mock
  private SelectionHandler selectionHandler;

  private Terrain terrain;

  @Before
  public void setUp() {
    terrain = new TerrainImpl(COORDINATE);
  }

  @Test
  public void select() {
    terrain.select(selectionHandler);

    verify(selectionHandler).select(terrain);
  }

  private static class TerrainImpl extends AbstractTerrain {

    TerrainImpl(Coordinate coordinate) {
      super(coordinate);
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