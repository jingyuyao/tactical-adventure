package com.jingyuyao.tactical.view;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.jingyuyao.tactical.Assets;
import com.jingyuyao.tactical.controller.MapActorController;
import com.jingyuyao.tactical.model.Character;
import com.jingyuyao.tactical.model.*;

import java.util.HashMap;

/**
 * Creates {@link MapActor} from models and adds the proper controllers.
 */
class MapActorFactory {
    private static final float ACTOR_SIZE = 1f; // world units

    private final AssetManager assetManager;
    private final java.util.Map<Terrain.Marker, Sprite> markerSpriteMap;
    private final java.util.Map<Class, Color> typeColorMap;

    MapActorFactory(AssetManager assetManager) {
        this.assetManager = assetManager;
        markerSpriteMap = new HashMap<Terrain.Marker, Sprite>();
        markerSpriteMap.put(
                Terrain.Marker.MOVE,
                new Sprite(assetManager.get(Assets.MOVE_OVERLAY, Texture.class))
        );
        markerSpriteMap.put(
                Terrain.Marker.DANGER,
                new Sprite(assetManager.get(Assets.DANGER_OVERLAY, Texture.class))
        );
        markerSpriteMap.put(
                Terrain.Marker.ATTACK,
                new Sprite(assetManager.get(Assets.ATTACK_OVERLAY, Texture.class))
        );
        typeColorMap = new HashMap<Class, Color>();
        typeColorMap.put(Player.class, Color.WHITE);
        typeColorMap.put(Enemy.class, Color.RED);
    }

    MapActor create(Map map, Selector selector, Character character) {
        return new CharacterActor(
                character,
                ACTOR_SIZE,
                new Sprite(assetManager.get("sprites/" + character.getName() + ".png", Texture.class)),
                typeColorMap.get(character.getClass()),
                new MapActorController(character, selector, map.getHighlighter(), ACTOR_SIZE)
        );
    }

    MapActor create(Map map, Selector selector, Terrain terrain) {
        return new TerrainActor(
                terrain,
                ACTOR_SIZE,
                markerSpriteMap,
                new MapActorController(terrain, selector, map.getHighlighter(), ACTOR_SIZE)
        );
    }
}
