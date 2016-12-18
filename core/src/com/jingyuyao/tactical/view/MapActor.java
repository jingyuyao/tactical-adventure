package com.jingyuyao.tactical.view;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.jingyuyao.tactical.controller.HighlightController;

/**
 * An {@link Actor} on a {@link MapView}.
 *
 * Invariants:
 * - getX() and getY() should ultimately match {@code mapObject.getX()} and {@code mapObject.getY()} after animations
 */
public class MapActor extends Actor {
    private static final float ACTOR_SIZE = 1f; // world units

    private Sprite sprite;

    /**
     * @param highlightController The listener used to detect highlight changes
     */
    MapActor(int initialX, int initialY, HighlightController highlightController) {
        setBounds(initialX, initialY, ACTOR_SIZE, ACTOR_SIZE);
        addListener(highlightController);
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

    float getSize() {
        return ACTOR_SIZE;
    }
}
