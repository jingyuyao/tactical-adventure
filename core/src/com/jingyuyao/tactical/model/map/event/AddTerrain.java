package com.jingyuyao.tactical.model.map.event;

import com.jingyuyao.tactical.model.event.AbstractEvent;
import com.jingyuyao.tactical.model.map.Terrain;

public class AddTerrain extends AbstractEvent<Terrain> {

  public AddTerrain(Terrain object) {
    super(object);
  }
}
