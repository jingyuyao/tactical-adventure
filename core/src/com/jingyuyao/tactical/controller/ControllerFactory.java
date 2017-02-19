package com.jingyuyao.tactical.controller;

import com.jingyuyao.tactical.model.map.MapObject;

public interface ControllerFactory {

  WorldActorController create(MapObject object);
}
