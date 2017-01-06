package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.Coordinate;
import com.jingyuyao.tactical.model.character.event.AddMarker;
import com.jingyuyao.tactical.model.character.event.RemoveMarker;
import com.jingyuyao.tactical.model.map.MapObject;
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
class BaseActor<T extends MapObject> extends Actor {

  private final T object;
  private final Map<Marker, Sprite> markerSpriteMap;
  private final List<Sprite> markerSprites;

  BaseActor(
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
  public void addMarker(AddMarker addMarker) {
    if (object.equals(addMarker.getObject())) {
      markerSprites.add(markerSpriteMap.get(addMarker.getMarker()));
    }
  }

  @Subscribe
  public void removeMarker(RemoveMarker removeMarker) {
    if (object.equals(removeMarker.getObject())) {
      markerSprites.remove(markerSpriteMap.get(removeMarker.getMarker()));
    }
  }
}
