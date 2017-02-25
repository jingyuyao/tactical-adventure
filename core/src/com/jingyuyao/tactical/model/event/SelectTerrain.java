package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.terrain.Terrain;

public class SelectTerrain extends SelectObject<Terrain> {

  public SelectTerrain(Terrain terrain) {
    super(terrain);
  }
}
