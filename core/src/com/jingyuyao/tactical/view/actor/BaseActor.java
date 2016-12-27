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
    private final Set<Sprite> markerSprites;

    /**
     * @param object This will be the first argument in {@link #update(Observable, Object)}
     */
    BaseActor(T object, float size, Waiter waiter, Map<Markers, Sprite> markerSpriteMap, EventListener listener) {
        this.waiter = waiter;
        this.markerSpriteMap = markerSpriteMap;
        markerSprites = new HashSet<Sprite>();
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
        if (AbstractObject.MarkerChange.class.isInstance(param)) {
            markerChange(AbstractObject.MarkerChange.class.cast(param));
        }
    }

    private void markerChange(AbstractObject.MarkerChange markerChange) {
        markerSprites.clear();
        for (Markers marker : markerChange.getMarkers()) {
            markerSprites.add(markerSpriteMap.get(marker));
        }
    }
}
