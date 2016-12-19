package com.jingyuyao.tactical.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.jingyuyao.tactical.model.Selector;
import com.jingyuyao.tactical.model.Terrain;

public class TerrainController extends MapActorController {
    private final Terrain terrain;

    public TerrainController(Selector selector, Terrain terrain, float actorSize) {
        super(selector, actorSize);
        this.terrain = terrain;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        Gdx.app.log("TerrainController", terrain.toString());

        getSelector().select(terrain);
    }
}
