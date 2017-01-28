package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.terrain.Terrain;

public class HighlightTerrain extends ObjectEvent<Terrain> {

  public HighlightTerrain(Terrain terrain) {
    super(terrain);
  }
}
