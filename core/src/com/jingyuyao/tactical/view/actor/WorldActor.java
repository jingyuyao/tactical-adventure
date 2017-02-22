package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.view.util.ViewUtil;
import java.util.LinkedHashSet;

public class WorldActor<T extends MapObject> extends Actor {

  private final T object;
  private final ActorConfig actorConfig;
  private final LinkedHashSet<Sprite> markers;

  WorldActor(T object, ActorConfig actorConfig, LinkedHashSet<Sprite> markers) {
    this.object = object;
    this.actorConfig = actorConfig;
    this.markers = markers;
    setSize(actorConfig.getActorWorldSize(), actorConfig.getActorWorldSize());
    updateCoordinate(object.getCoordinate());
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    for (Sprite sprite : markers) {
      ViewUtil.draw(batch, sprite, this);
    }
  }

  public void addMarker(Sprite sprite) {
    markers.add(sprite);
  }

  public void clearMarkers() {
    markers.clear();
  }

  void updateCoordinate(Coordinate coordinate) {
    setPosition(
        coordinate.getX() * actorConfig.getActorWorldSize(),
        coordinate.getY() * actorConfig.getActorWorldSize());
  }

  T getObject() {
    return object;
  }

  ActorConfig getActorConfig() {
    return actorConfig;
  }
}
