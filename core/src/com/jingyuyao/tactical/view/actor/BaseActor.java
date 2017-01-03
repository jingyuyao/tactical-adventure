package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.Waiter;
import com.jingyuyao.tactical.model.character.event.AddMarker;
import com.jingyuyao.tactical.model.character.event.RemoveMarker;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.mark.Marker;
import com.jingyuyao.tactical.view.MapView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * An {@link Actor} on a {@link MapView}.
 * Draws all {@link Marker} that belongs to {@link MapObject} in no particular order.
 *
 * Invariants:
 * - getX() and getY() should ultimately match {@code mapObject.getX()} and {@code mapObject.getY()} after animations
 */
class BaseActor<T extends MapObject> extends Actor {
    private final T object;
    private final Waiter waiter;
    private final Map<Marker, Sprite> markerSpriteMap;
    private final List<Sprite> markerSprites;

    BaseActor(EventBus eventBus, T object, float size, Waiter waiter, Map<Marker, Sprite> markerSpriteMap, EventListener listener) {
        this.object = object;
        this.waiter = waiter;
        this.markerSpriteMap = markerSpriteMap;
        markerSprites = new ArrayList<Sprite>();
        setBounds(object.getCoordinate().getX(), object.getCoordinate().getY(), size, size);
        addListener(listener);
        eventBus.register(this);
    }

    T getObject() {
        return object;
    }

    Waiter getWaiter() {
        return waiter;
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
