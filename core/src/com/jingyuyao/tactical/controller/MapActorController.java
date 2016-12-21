package com.jingyuyao.tactical.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.jingyuyao.tactical.model.Map;
import com.jingyuyao.tactical.model.MapObject;
import com.jingyuyao.tactical.model.state.MapState;

/**
 * Only dispatches clicked events if the click begins and ends over the actor.
 */
public class MapActorController extends ClickListener {
    private final MapObject object;
    private final MapState mapState;
    private final Map map;

    public MapActorController(MapObject object, MapState mapState, Map map, float actorSize) {
        this.object = object;
        this.mapState = mapState;
        this.map = map;
        setTapSquareSize(actorSize / 2f);
    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        super.enter(event, x, y, pointer, fromActor);
        map.setHighlight(object);
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        Gdx.app.log("MapActorController", object.toString());
        object.select(mapState);
    }
}
