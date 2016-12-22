package com.jingyuyao.tactical.view;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.jingyuyao.tactical.Assets;
import com.jingyuyao.tactical.controller.MapActorController;
import com.jingyuyao.tactical.model.*;
import com.jingyuyao.tactical.model.state.MapState;

import java.util.HashMap;

/**
 * Creates {@link MapActor} from models and adds the proper controllers.
 */
class ActorFactory {
    static final float ACTOR_SIZE = 1f; // world units

    private final AssetManager assetManager;
    private final java.util.Map<Terrain.Marker, Sprite> markerSpriteMap;
    private final java.util.Map<Class, Color> typeColorMap;

    ActorFactory(AssetManager assetManager) {
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

    Actor create(Map map, MapState mapState, Player player) {
        return new PlayerActor(
                player,
                ACTOR_SIZE,
                new Sprite(assetManager.get("sprites/" + player.getName() + ".png", Texture.class)),
                typeColorMap.get(player.getClass()),
                new MapActorController(map, mapState, player, ACTOR_SIZE)
        );
    }

    Actor create(Map map, MapState mapState, Enemy enemy) {
        return new CharacterActor<Enemy>(
                enemy,
                ACTOR_SIZE,
                new Sprite(assetManager.get("sprites/" + enemy.getName() + ".png", Texture.class)),
                typeColorMap.get(enemy.getClass()),
                new MapActorController(map, mapState, enemy, ACTOR_SIZE)
        );
    }

    Actor create(Map map, MapState mapState, Terrain terrain) {
        return new TerrainActor(
                terrain,
                ACTOR_SIZE,
                markerSpriteMap,
                new MapActorController(map, mapState, terrain, ACTOR_SIZE)
        );
    }
}
