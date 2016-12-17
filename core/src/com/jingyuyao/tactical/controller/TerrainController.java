package com.jingyuyao.tactical.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.jingyuyao.tactical.model.Map;
import com.jingyuyao.tactical.model.Terrain;

public class TerrainController extends InputListener {
    private final Map map;
    private final Terrain terrain;

    TerrainController(Map map, Terrain terrain) {
        this.map = map;
        this.terrain = terrain;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        Gdx.app.log("TerrainController", terrain.toString());

        // TODO: Do we need to suspend input while moving? probably need to add to game controller
        switch (terrain.getPotentialTarget()) {
            case NONE:
                map.deselect();
                break;
            case REACHABLE:
                map.moveSelectedTo(terrain);
                break;
            case ATTACKABLE:
                break;
        }

        return false;
    }
}
