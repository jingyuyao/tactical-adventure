package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.map.MapObject;

public class SelectObject<T extends MapObject> extends ObjectEvent<T> {

  protected SelectObject(T object) {
    super(object);
  }
}
