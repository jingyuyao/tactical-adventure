package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.jingyuyao.tactical.model.Markers;
import com.jingyuyao.tactical.model.Waiter;
import com.jingyuyao.tactical.model.object.AbstractObject;
import com.jingyuyao.tactical.view.MapView;

import java.util.*;

/**
 * An {@link Actor} on a {@link MapView}.
 * Draws all {@link Markers} that belongs to {@link AbstractObject} in no particular order.
 *
 * Invariants:
 * - getX() and getY() should ultimately match {@code mapObject.getX()} and {@code mapObject.getY()} after animations
 */
public class BaseActor<T extends AbstractObject> extends Actor implements Observer {
    private final Waiter waiter;
    private final Map<Markers, Sprite> markerSpriteMap;
    private final List<Sprite> markerSprites;

    /**
     * @param object This will be the first argument in {@link #update(Observable, Object)}
     */
    BaseActor(T object, float size, Waiter waiter, Map<Markers, Sprite> markerSpriteMap, EventListener listener) {
        this.waiter = waiter;
        this.markerSpriteMap = markerSpriteMap;
        markerSprites = new ArrayList<Sprite>();
        setBounds(object.getCoordinate().getX(), object.getCoordinate().getY(), size, size);
        addListener(listener);
        object.addObserver(this);
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

    @Override
    public void update(Observable observable, Object param) {
        if (AbstractObject.AddMarker.class.isInstance(param)) {
            addMarker(AbstractObject.AddMarker.class.cast(param));
        } else if (AbstractObject.RemoveMarker.class.isInstance(param)) {
            removeMarker(AbstractObject.RemoveMarker.class.cast(param));
        }
    }

    private void addMarker(AbstractObject.AddMarker addMarker) {
        markerSprites.add(markerSpriteMap.get(addMarker.getMarker()));
    }

    private void removeMarker(AbstractObject.RemoveMarker removeMarker) {
        markerSprites.remove(markerSpriteMap.get(removeMarker.getMarker()));
    }
}
