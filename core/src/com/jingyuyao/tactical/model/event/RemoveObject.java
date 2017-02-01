package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.map.MapObject;

public class RemoveObject extends ObjectEvent<MapObject> {

  public RemoveObject(MapObject object) {
    super(object);
  }
}
