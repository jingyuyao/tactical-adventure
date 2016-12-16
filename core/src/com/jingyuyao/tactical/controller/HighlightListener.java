package com.jingyuyao.tactical.controller;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

/**
 * Reports whether this actor is highlighted.
 * Highlighted = touched/hovered over.
 */
public class HighlightListener extends InputListener {
    private boolean highlighted = false;
    // Prevents disabling highlight from clicking the actor
    private boolean exitFromTouch = false;

    public boolean isHighlighted() {
        return highlighted;
    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        highlighted = true;
        exitFromTouch = false;
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
        return true;
    }

    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        exitFromTouch = true;
    }
}