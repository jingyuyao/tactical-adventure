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
    private final java.util.Map<Terrain.SelectionMode, Sprite> selectionModeSpriteMap;
    private final java.util.Map<Character.Type, Color> typeColorMap;

    MapActorFactory(AssetManager assetManager) {
        this.assetManager = assetManager;
        selectionModeSpriteMap = new HashMap<Terrain.SelectionMode, Sprite>();
        selectionModeSpriteMap.put(
                Terrain.SelectionMode.MOVE,
                new Sprite(assetManager.get(Assets.BLUE_OVERLAY, Texture.class))
        );
        typeColorMap = new HashMap<Character.Type, Color>();
        typeColorMap.put(Character.Type.PLAYER, Color.WHITE);
        typeColorMap.put(Character.Type.ENEMY, Color.RED);
        typeColorMap.put(Character.Type.NEUTRAL, Color.GREEN);
    }

    MapActor create(Map map, Character character) {
        return new CharacterActor(
                character,
                ACTOR_SIZE,
                new Sprite(assetManager.get("sprites/" + character.getName() + ".png", Texture.class)),
                typeColorMap,
                new HighlightController(map, character),
                new CharacterController(map, character, ACTOR_SIZE)
        );
    }

    MapActor create(Map map, Terrain terrain) {
        return new TerrainActor(
                terrain,
                ACTOR_SIZE,
                selectionModeSpriteMap,
                new HighlightController(map, terrain),
                new TerrainController(map, terrain, ACTOR_SIZE)
        );
    }
}
