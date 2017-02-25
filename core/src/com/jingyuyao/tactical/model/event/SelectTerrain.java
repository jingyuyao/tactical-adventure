package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.terrain.Terrain;

public class SelectTerrain extends ObjectEvent<Terrain> {

  public SelectTerrain(Terrain terrain) {
    super(terrain);
  }
}
