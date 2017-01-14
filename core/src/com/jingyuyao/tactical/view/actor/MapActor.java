package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.map.event.SyncMarkers;
import com.jingyuyao.tactical.model.mark.Marker;
import com.jingyuyao.tactical.view.MapView;
import java.util.List;
import java.util.Map;

/**
 * An {@link Actor} on a {@link MapView}. Draws all {@link Marker} that belongs to {@link MapObject}
 * in no particular order.
 *
 * <p>Invariants: - getX() and getY() should ultimately match {@code mapObject.getX()} and {@code
 * mapObject.getY()} after animations
 */
class MapActor<T extends MapObject> extends Actor {

  private final T object;
  private final Map<Marker, Sprite> markerSpriteMap;
  private final List<Sprite> markerSprites;

  MapActor(
      T object,
      EventListener listener,
      float size,
      EventBus eventBus,
      Map<Marker, Sprite> markerSpriteMap,
      List<Sprite> markerSprites) {
    this.object = object;
    this.markerSpriteMap = markerSpriteMap;
    this.markerSprites = markerSprites;
    Coordinate coordinate = object.getCoordinate();
    setBounds(coordinate.getX() * size, coordinate.getY() * size, size, size);
    addListener(listener);
    eventBus.register(this);
  }

  T getObject() {
    return object;
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    for (Sprite sprite : markerSprites) {
      sprite.setBounds(getX(), getY(), getWidth(), getHeight());
      sprite.draw(batch);
    }
  }

  @Subscribe
  public void markersChanged(SyncMarkers syncMarkers) {
    if (syncMarkers.matches(object)) {
      markerSprites.clear();
      Iterables.addAll(
          markerSprites,
          Iterables.transform(
              syncMarkers.getMarkers(),
              new Function<Marker, Sprite>() {
                @Override
                public Sprite apply(Marker input) {
                  return markerSpriteMap.get(input);
                }
              }));
    }
  }
}
