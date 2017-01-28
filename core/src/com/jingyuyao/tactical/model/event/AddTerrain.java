package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.terrain.Terrain;

public class AddTerrain extends ObjectEvent<Terrain> {

  public AddTerrain(Terrain object) {
    super(object);
  }
}
