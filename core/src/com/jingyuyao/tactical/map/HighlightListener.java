package com.jingyuyao.tactical.map;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

/**
 * Reports whether this actor is highlighted.
 * Highlighted = touched/hovered over.
 */
class HighlightListener extends InputListener {
    private boolean highlighted = false;
    // Prevents disabling highlight from clicking the actor
    private boolean exitFromTouch = false;

    boolean isHighlighted() {
        return highlighted;
    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        highlighted = true;
    }

    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        if (exitFromTouch) {
            exitFromTouch = false;
        } else {
            highlighted = false;
        }
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        exitFromTouch = true;
        return false;
    }
}
