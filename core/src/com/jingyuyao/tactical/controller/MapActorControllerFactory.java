package com.jingyuyao.tactical.controller;

import com.jingyuyao.tactical.model.map.MapObject;

public interface MapActorControllerFactory {

  MapActorController create(MapObject object);
}
