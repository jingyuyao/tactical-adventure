package com.jingyuyao.tactical.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.jingyuyao.tactical.model.MapObject;
import com.jingyuyao.tactical.model.Selector;

/**
 * Only dispatches clicked events if the click begins and ends over the actor.
 */
public class MapActorController extends ClickListener {
    private final MapObject object;
    private final Selector selector;

    public MapActorController(MapObject object, Selector selector, float actorSize) {
        this.object = object;
        this.selector = selector;
        setTapSquareSize(actorSize / 2f);
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        Gdx.app.log("MapActorController", object.toString());

        object.select(selector);
    }
}
