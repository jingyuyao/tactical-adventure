package com.jingyuyao.tactical.controller;

import com.jingyuyao.tactical.model.map.MapObject;

public interface ControllerFactory {

  MapActorController create(MapObject object);
}
