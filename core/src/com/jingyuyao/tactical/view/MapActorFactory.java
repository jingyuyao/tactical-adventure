package com.jingyuyao.tactical.view;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.jingyuyao.tactical.controller.CharacterController;
import com.jingyuyao.tactical.controller.HighlightController;
import com.jingyuyao.tactical.controller.TerrainController;
import com.jingyuyao.tactical.model.Character;
import com.jingyuyao.tactical.model.Map;
import com.jingyuyao.tactical.model.Terrain;

/**
 * Creates {@link MapActor} from models and adds the proper controllers.
 */
class MapActorFactory {
    private static final float ACTOR_SIZE = 1f; // world units

    private final AssetManager assetManager;

    MapActorFactory(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    MapActor createCharacter(Map map, Character character) {
        return new MapActor(
                character,
                ACTOR_SIZE,
                new Sprite(assetManager.get("sprites/" + character.getName() + ".png", Texture.class)),
                new HighlightController(map, character),
                new CharacterController(map, character, ACTOR_SIZE)
        );
    }

    MapActor createTerrain(Map map, Terrain terrain) {
        return new MapActor(
                terrain,
                ACTOR_SIZE,
                new HighlightController(map, terrain),
                new TerrainController(map, terrain, ACTOR_SIZE)
        );
    }
}
