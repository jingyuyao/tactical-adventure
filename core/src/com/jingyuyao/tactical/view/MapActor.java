package com.jingyuyao.tactical.view;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.jingyuyao.tactical.controller.HighlightController;
import com.jingyuyao.tactical.model.MapObject;

/**
 * An {@link Actor} on a {@link MapView}.
 *
 * This is responsible for rendering the game's objects as well as relaying player actions
 * to the them.
 *
 * Invariants:
 * - getX() and getY() should ultimately match {@code mapObject.getX()} and {@code mapObject.getY()} after animations
 */
public class MapActor extends Actor {
    private static final float ACTOR_SIZE = 1f; // world units

    private final MapObject mapObject;
    private final Sprite highlight;
    private Sprite sprite;

    /**
     * Actor's initial position is set to the position of {@code mapObject}.
     * @param mapObject The game mapObject contained by this actor
     * @param highlight The highlight sprite
     * @param highlightController The listener used to detect highlight changes
     */
    MapActor(MapObject mapObject, Sprite highlight, HighlightController highlightController) {
        this.mapObject = mapObject;
        this.highlight = highlight;
        setBounds(mapObject.getX(), mapObject.getY(), ACTOR_SIZE, ACTOR_SIZE);
        addListener(highlightController);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (sprite != null) {
            sprite.setBounds(getX(), getY(), getWidth(), getHeight());
            sprite.draw(batch);
        }

        if (mapObject.isHighlighted()) {
            highlight.setBounds(getX(), getY(), getWidth(), getHeight());
            highlight.draw(batch);
        }
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }
}
