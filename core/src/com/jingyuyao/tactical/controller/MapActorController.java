package com.jingyuyao.tactical.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.jingyuyao.tactical.model.Highlighter;
import com.jingyuyao.tactical.model.object.AbstractObject;
import com.jingyuyao.tactical.model.state.MapState;

/**
 * Only dispatches clicked events if the click begins and ends over the actor.
 */
public class MapActorController extends ClickListener {
    // TODO: Is this GC'ed?
    private final AbstractObject object;
    private final MapState mapState;
    private final Highlighter highlighter;

    public MapActorController(MapState mapState, Highlighter highlighter, AbstractObject object, float actorSize) {
        this.object = object;
        this.mapState = mapState;
        this.highlighter = highlighter;
        setTapSquareSize(actorSize / 2f);
    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        super.enter(event, x, y, pointer, fromActor);
        object.highlight(highlighter);
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        Gdx.app.log("MapActorController", object.toString());
        object.select(mapState);
    }
}
