package com.jingyuyao.tactical.controller;

import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.jingyuyao.tactical.model.Selector;

/**
 * Only dispatches clicked events if the click begins and ends over the actor.
 */
abstract class MapActorController extends ClickListener {
    private final Selector selector;

    MapActorController(Selector selector, float actorSize) {
        this.selector = selector;
        setTapSquareSize(actorSize / 2f);
    }

    Selector getSelector() {
        return selector;
    }
}
