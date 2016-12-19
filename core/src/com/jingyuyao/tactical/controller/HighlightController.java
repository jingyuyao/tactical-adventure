package com.jingyuyao.tactical.controller;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.jingyuyao.tactical.model.Highlighter;
import com.jingyuyao.tactical.model.MapObject;

/**
 * Manages the currently highlighted object.
 * Highlighted = touched/hovered over.
 */
public class HighlightController extends InputListener {
    private final Highlighter highlighter;
    private final MapObject mapObject;

    public HighlightController(Highlighter highlighter, MapObject mapObject) {
        this.highlighter = highlighter;
        this.mapObject = mapObject;
    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        highlighter.setHighlight(mapObject);
    }
}
