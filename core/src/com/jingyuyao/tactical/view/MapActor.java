package com.jingyuyao.tactical.view;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.jingyuyao.tactical.model.HasCoordinate;

/**
 * An {@link Actor} on a {@link MapView}.
 *
 * Eh, this class turns out dumber than I expected :/
 *
 * Invariants:
 * - getX() and getY() should ultimately match {@code mapObject.getX()} and {@code mapObject.getY()} after animations
 */
public class MapActor extends Actor {
    private Sprite sprite;

    MapActor(HasCoordinate coordinate, float size, Sprite sprite, EventListener... listeners) {
        this.sprite = sprite;
        setBounds(coordinate.getX(), coordinate.getY(), size, size);
        for (EventListener listener : listeners) {
            addListener(listener);
        }
    }

    MapActor(HasCoordinate coordinate, float size, EventListener... listeners) {
        this(coordinate, size, null, listeners);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (sprite != null) {
            sprite.setBounds(getX(), getY(), getWidth(), getHeight());
            sprite.draw(batch);
        }
    }

    void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }
}
