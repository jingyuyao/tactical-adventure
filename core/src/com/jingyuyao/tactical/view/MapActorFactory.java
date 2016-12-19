package com.jingyuyao.tactical.view;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.jingyuyao.tactical.Assets;
import com.jingyuyao.tactical.controller.CharacterController;
import com.jingyuyao.tactical.controller.HighlightController;
import com.jingyuyao.tactical.controller.TerrainController;
import com.jingyuyao.tactical.model.Character;
import com.jingyuyao.tactical.model.Map;
import com.jingyuyao.tactical.model.Terrain;

import java.util.HashMap;

/**
 * Creates {@link MapActor} from models and adds the proper controllers.
 */
class MapActorFactory {
    private static final float ACTOR_SIZE = 1f; // world units

    private final AssetManager assetManager;
    private final java.util.Map<Terrain.Marker, Sprite> markerSpriteMap;
    private final java.util.Map<Character.Type, Color> typeColorMap;

    MapActorFactory(AssetManager assetManager) {
        this.assetManager = assetManager;
        markerSpriteMap = new HashMap<Terrain.Marker, Sprite>();
        markerSpriteMap.put(
                Terrain.Marker.MOVE,
                new Sprite(assetManager.get(Assets.BLUE_OVERLAY, Texture.class))
        );
        markerSpriteMap.put(
                Terrain.Marker.DANGER,
                new Sprite(assetManager.get(Assets.RED_OVERLAY, Texture.class))
        );
        typeColorMap = new HashMap<Character.Type, Color>();
        typeColorMap.put(Character.Type.PLAYER, Color.WHITE);
        typeColorMap.put(Character.Type.ENEMY, Color.RED);
    }

    MapActor create(Map map, Character character) {
        return new CharacterActor(
                character,
                ACTOR_SIZE,
                new Sprite(assetManager.get("sprites/" + character.getName() + ".png", Texture.class)),
                typeColorMap,
                new HighlightController(map, character),
                new CharacterController(map.getLogic(), character, ACTOR_SIZE)
        );
    }

    MapActor create(Map map, Terrain terrain) {
        return new TerrainActor(
                terrain,
                ACTOR_SIZE,
                markerSpriteMap,
                new HighlightController(map, terrain),
                new TerrainController(map.getLogic(), terrain, ACTOR_SIZE)
        );
    }
}
