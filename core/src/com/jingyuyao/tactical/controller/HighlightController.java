package com.jingyuyao.tactical.controller;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.jingyuyao.tactical.model.MapObject;

/**
 * Manages the currently highlighted object.
 * Highlighted = touched/hovered over.
 */
public class HighlightController extends InputListener {
    private static MapObject lastHighlighted;

    private final MapObject mapObject;

    HighlightController(MapObject mapObject) {
        this.mapObject = mapObject;
    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        if (lastHighlighted != mapObject) {
            if (lastHighlighted != null) {
                lastHighlighted.setHighlighted(false);
            }
            mapObject.setHighlighted(true);
            lastHighlighted = mapObject;
        }
    }
}
