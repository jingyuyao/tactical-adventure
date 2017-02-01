package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.google.common.collect.Multiset;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.map.Marker;
import com.jingyuyao.tactical.view.MapView;

/**
 * An {@link Actor} on a {@link MapView}. Draws all {@link Marker} that belongs to {@link MapObject}
 * in no particular order.
 *
 * <p>Invariants: - getX() and getY() should ultimately match {@code mapObject.getX()} and {@code
 * mapObject.getY()} after animations
 */
public class MapActor<T extends MapObject> extends Actor {

  private final T object;
  private final Multiset<Sprite> markerSprites;

  MapActor(T object, EventListener listener, float size, Multiset<Sprite> markerSprites) {
    this.object = object;
    this.markerSprites = markerSprites;
    Coordinate coordinate = object.getCoordinate();
    setBounds(coordinate.getX() * size, coordinate.getY() * size, size, size);
    addListener(listener);
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    // We allow actor to retain multiple copies of a marker sprite but we only show one at a time
    // this allows multiple markings to apply and remove the same marker on an actor without
    // worrying about other markings
    for (Sprite sprite : markerSprites.elementSet()) {
      sprite.setBounds(getX(), getY(), getWidth(), getHeight());
      sprite.draw(batch);
    }
  }

  public void addMarkerSprite(Sprite sprite) {
    markerSprites.add(sprite);
  }

  public void removeMarkerSprite(Sprite sprite) {
    markerSprites.remove(sprite);
  }

  T getObject() {
    return object;
  }
}
