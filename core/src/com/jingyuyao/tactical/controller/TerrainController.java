package com.jingyuyao.tactical.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.jingyuyao.tactical.model.Map;
import com.jingyuyao.tactical.model.Terrain;

public class TerrainController extends MapActorController {
    private final Terrain terrain;

    public TerrainController(Map map, Terrain terrain, float actorSize) {
        super(map, actorSize);
        this.terrain = terrain;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        Gdx.app.log("TerrainController", terrain.toString());

        switch (terrain.getPotentialTarget()) {
            case NONE:
                getMap().deselect();
                break;
            case REACHABLE:
                getMap().moveSelectedTo(terrain);
                break;
            case ATTACKABLE:
                break;
        }
    }
}
