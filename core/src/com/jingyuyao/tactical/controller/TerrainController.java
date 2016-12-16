package com.jingyuyao.tactical.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.jingyuyao.tactical.model.Character;
import com.jingyuyao.tactical.model.Map;
import com.jingyuyao.tactical.model.MapObject;
import com.jingyuyao.tactical.model.Terrain;
import com.jingyuyao.tactical.view.MapActor;

public class TerrainController extends InputListener {
    private final Map map;
    private final Terrain terrain;
    private final java.util.Map<MapObject, MapActor> actorMap;

    TerrainController(Map map, Terrain terrain, java.util.Map<MapObject, MapActor> actorMap) {
        this.map = map;
        this.terrain = terrain;
        this.actorMap = actorMap;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        Gdx.app.log("TerrainController", terrain.toString());

        // TODO: Need to check if within range, time to bust out Dijkstra's!
        if (map.getSelected() != null) {
            Character selected = map.getSelected();
            MapActor selectedActor = actorMap.get(selected);

            MoveToAction move = new MoveToAction();
            move.setPosition(terrain.getX(), terrain.getY());
            move.setDuration(0.5f);

            selected.setPosition(terrain.getX(), terrain.getY());
            selectedActor.addAction(move);
            map.setSelected(null);
        }

        return false;
    }
}
