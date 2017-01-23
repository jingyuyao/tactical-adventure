package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.google.common.collect.Multiset;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.mark.Marker;
import com.jingyuyao.tactical.view.MapView;
import com.jingyuyao.tactical.view.actor.ActorAnnotations.ActorWorldSize;
import com.jingyuyao.tactical.view.actor.ActorAnnotations.InitialMarkerSprites;

/**
 * An {@link Actor} on a {@link MapView}. Draws all {@link Marker} that belongs to {@link MapObject}
 * in no particular order.
 *
 * <p>Invariants: - getX() and getY() should ultimately match {@code mapObject.getX()} and {@code
 * mapObject.getY()} after animations
 */
public class MapActor extends Actor {

  private final Multiset<Sprite> markerSprites;
  private Sprite sprite;

  @AssistedInject
  MapActor(
      @Assisted Coordinate initialCoordinate,
      @Assisted EventListener listener,
      @ActorWorldSize float size,
      @InitialMarkerSprites Multiset<Sprite> markerSprites) {
    this(initialCoordinate, listener, null, Color.WHITE, size, markerSprites);
  }

  @AssistedInject
  MapActor(
      @Assisted Coordinate initialCoordinate,
      @Assisted EventListener listener,
      @Assisted Sprite sprite,
      @Assisted Color initialColor,
      @ActorWorldSize float size,
      @InitialMarkerSprites Multiset<Sprite> markerSprites) {
    this.sprite = sprite;
    this.markerSprites = markerSprites;
    setColor(initialColor);
    setBounds(initialCoordinate.getX() * size, initialCoordinate.getY() * size, size, size);
    addListener(listener);
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    if (sprite != null) {
      sprite.setColor(getColor());
      sprite.setBounds(getX(), getY(), getWidth(), getHeight());
      sprite.draw(batch);
    }
    // We allow actor to retain multiple copies of a marker sprite but we only show one at a time
    // this allows multiple markings to apply and remove the same marker on an actor without
    // worrying about other markings
    for (Sprite sprite : markerSprites.elementSet()) {
      sprite.setBounds(getX(), getY(), getWidth(), getHeight());
      sprite.draw(batch);
    }
  }

  void addMarkerSprite(Sprite markerSprite) {
    markerSprites.add(markerSprite);
  }

  void removeMarkerSprite(Sprite markerSprite) {
    markerSprites.remove(markerSprite);
  }
}
