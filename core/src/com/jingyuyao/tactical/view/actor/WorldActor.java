package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.view.resource.WorldTexture;
import java.util.LinkedHashSet;

public class WorldActor<T extends MapObject> extends Actor {

  private final T object;
  private final ActorConfig actorConfig;
  private final LinkedHashSet<WorldTexture> markers;

  WorldActor(T object, ActorConfig actorConfig, LinkedHashSet<WorldTexture> markers) {
    this.object = object;
    this.actorConfig = actorConfig;
    this.markers = markers;
    setSize(actorConfig.getActorWorldSize(), actorConfig.getActorWorldSize());
    updateCoordinate(object.getCoordinate());
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    for (WorldTexture worldTexture : markers) {
      worldTexture.draw(batch, this);
    }
  }

  public void addMarker(WorldTexture worldTexture) {
    markers.add(worldTexture);
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
