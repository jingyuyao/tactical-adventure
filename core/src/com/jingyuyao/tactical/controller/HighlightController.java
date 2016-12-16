package com.jingyuyao.tactical.controller;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.jingyuyao.tactical.model.Map;
import com.jingyuyao.tactical.model.MapObject;

/**
 * Manages the currently highlighted object.
 * Highlighted = touched/hovered over.
 */
public class HighlightController extends InputListener {
    private final Map map;
    private final MapObject mapObject;

    HighlightController(Map map, MapObject mapObject) {
        this.map = map;
        this.mapObject = mapObject;
    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        MapObject lastHighlighted = map.getHighlighted();
        if (lastHighlighted != mapObject) {
            if (lastHighlighted != null) {
                lastHighlighted.setHighlighted(false);
            }
            mapObject.setHighlighted(true);
            map.setHighlighted(mapObject);
        }
    }
}
