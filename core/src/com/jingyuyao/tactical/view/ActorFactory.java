package com.jingyuyao.tactical.view;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.jingyuyao.tactical.Assets;
import com.jingyuyao.tactical.controller.MapActorController;
import com.jingyuyao.tactical.model.Enemy;
import com.jingyuyao.tactical.model.Level;
import com.jingyuyao.tactical.model.Player;
import com.jingyuyao.tactical.model.Terrain;

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

    Actor create(Level level, Player player) {
        return new PlayerActor(
                player,
                ACTOR_SIZE,
                level.getAnimationCounter(),
                new Sprite(assetManager.get("sprites/" + player.getName() + ".png", Texture.class)),
                typeColorMap.get(player.getClass()),
                new MapActorController(level.getMap(), level.getMapState(), player, ACTOR_SIZE)
        );
    }

    Actor create(Level level, Enemy enemy) {
        return new CharacterActor<Enemy>(
                enemy,
                ACTOR_SIZE,
                level.getAnimationCounter(),
                new Sprite(assetManager.get("sprites/" + enemy.getName() + ".png", Texture.class)),
                typeColorMap.get(enemy.getClass()),
                new MapActorController(level.getMap(), level.getMapState(), enemy, ACTOR_SIZE)
        );
    }

    Actor create(Level level, Terrain terrain) {
        return new TerrainActor(
                terrain,
                ACTOR_SIZE,
                level.getAnimationCounter(),
                markerSpriteMap,
                new MapActorController(level.getMap(), level.getMapState(), terrain, ACTOR_SIZE)
        );
    }
}