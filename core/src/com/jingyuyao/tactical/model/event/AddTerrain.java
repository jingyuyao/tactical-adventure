package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.map.Terrain;

public class AddTerrain extends ObjectEvent<Terrain> {

  public AddTerrain(Terrain object) {
    super(object);
  }
}
