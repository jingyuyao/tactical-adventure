package com.jingyuyao.tactical.model.state.event;

import com.jingyuyao.tactical.model.event.AbstractEvent;
import com.jingyuyao.tactical.model.map.Terrain;

public class HighlightTerrain extends AbstractEvent<Terrain> {

  public HighlightTerrain(Terrain terrain) {
    super(terrain);
  }
}
