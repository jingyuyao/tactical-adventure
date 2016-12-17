package com.jingyuyao.tactical.controller;

import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.jingyuyao.tactical.model.Map;

/**
 * Only dispatches clicked events if the click begins and ends over the actor.
 */
class MapActorController extends ClickListener {
    private final Map map;

    MapActorController(Map map, float actorSize) {
        this.map = map;
        setTapSquareSize(actorSize / 2f);
    }

    Map getMap() {
        return map;
    }
}
