package com.jingyuyao.tactical.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.jingyuyao.tactical.model.Character;
import com.jingyuyao.tactical.model.*;
import com.jingyuyao.tactical.view.MapActor;

import java.util.Set;

public class CharacterController extends InputListener {
    private final Map map;
    private final Character character;
    private final java.util.Map<MapObject, MapActor> actorMap;
    private final Sprite blueOverlay;

    CharacterController(Map map, Character character, java.util.Map<MapObject, MapActor> actorMap, Sprite blueOverlay) {
        this.map = map;
        this.character = character;
        this.actorMap = actorMap;
        this.blueOverlay = blueOverlay;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        Gdx.app.log("CharacterController", character.toString());

        // Unselected selected character
        if (character.equals(map.getSelected())) {
            map.setSelected(null);
            return false;
        }

        map.setSelected(character);
        Terrain characterTerrain = map.getTerrain(character.getX(), character.getY());
        Path reachablePath = map.getReachablePath(characterTerrain, 2);
        Set<Terrain> reachableTerrains = reachablePath.getReachableTerrains();
        for (Terrain terrain : reachableTerrains) {
            terrain.setPotentialTarget(Terrain.PotentialTarget.MOVE);
            MapActor actor = this.actorMap.get(terrain);
            actor.setSprite(blueOverlay);
        }

        return false;
    }
}
