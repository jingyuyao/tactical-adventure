package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.jingyuyao.tactical.model.common.Coordinate;

public interface ActorFactory {

  MapActor create(Coordinate initialCoordinate, EventListener eventListener);

  MapActor create(
      Coordinate initialCoordinate, EventListener eventListener, Sprite sprite, Color initialColor);
}
