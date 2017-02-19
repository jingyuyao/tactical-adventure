package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.map.MapObject;
import java.util.LinkedHashSet;

/**
 * <p>Invariants: - getX() and getY() should ultimately match {@code mapObject.getX()} and {@code
 * mapObject.getY()} after animations
 */
public class WorldActor<T extends MapObject> extends Actor {

  private final T object;
  private final ActorConfig actorConfig;
  private final LinkedHashSet<Sprite> markers;

  WorldActor(T object, ActorConfig actorConfig, LinkedHashSet<Sprite> markers) {
    this.object = object;
    this.actorConfig = actorConfig;
    this.markers = markers;
    Coordinate coordinate = object.getCoordinate();
    float size = actorConfig.getActorWorldSize();
    setBounds(coordinate.getX() * size, coordinate.getY() * size, size, size);
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    for (Sprite sprite : markers) {
      sprite.setBounds(getX(), getY(), getWidth(), getHeight());
      sprite.draw(batch);
    }
  }

  public void addMarker(Sprite sprite) {
    markers.add(sprite);
  }

  public void clearMarkers() {
    markers.clear();
  }

  T getObject() {
    return object;
  }

  ActorConfig getActorConfig() {
    return actorConfig;
  }
}
