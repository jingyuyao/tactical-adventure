package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.jingyuyao.tactical.Assets;
import com.jingyuyao.tactical.controller.MapActorController;
import com.jingyuyao.tactical.model.Level;
import com.jingyuyao.tactical.model.Marker;
import com.jingyuyao.tactical.model.object.Enemy;
import com.jingyuyao.tactical.model.object.Player;
import com.jingyuyao.tactical.model.object.Terrain;

import java.util.HashMap;

/**
 * Creates {@link BaseActor} from models and adds the proper controllers.
 */
public class ActorFactory {
    public static final float ACTOR_SIZE = 1f; // world units

    private final AssetManager assetManager;
    private final java.util.Map<Marker, Sprite> markerSpriteMap;
    private final java.util.Map<Class, Color> typeColorMap;

    public ActorFactory(AssetManager assetManager) {
        this.assetManager = assetManager;
        markerSpriteMap = new HashMap<Marker, Sprite>();
        markerSpriteMap.put(
                Marker.HIGHLIGHT,
                new Sprite(assetManager.get(Assets.HIGHLIGHT, Texture.class))
        );
        markerSpriteMap.put(
                Marker.CAN_MOVE_TO,
                new Sprite(assetManager.get(Assets.MOVE, Texture.class))
        );
        markerSpriteMap.put(
                Marker.DANGER,
                new Sprite(assetManager.get(Assets.DANGER, Texture.class))
        );
        markerSpriteMap.put(
                Marker.CAN_ATTACK,
                new Sprite(assetManager.get(Assets.ATTACK, Texture.class))
        );
        markerSpriteMap.put(
                Marker.POTENTIAL_TARGET,
                new Sprite(assetManager.get(Assets.POTENTIAL_TARGET, Texture.class))
        );
        markerSpriteMap.put(
                Marker.CHOSEN_TARGET,
                new Sprite(assetManager.get(Assets.CHOSEN_TARGET, Texture.class))
        );
        typeColorMap = new HashMap<Class, Color>();
        typeColorMap.put(Player.class, Color.WHITE);
        typeColorMap.put(Enemy.class, Color.RED);
    }

    public Actor create(Level level, Player player) {
        return new PlayerActor(
                player,
                ACTOR_SIZE,
                level.getWaiter(),
                markerSpriteMap,
                new Sprite(assetManager.get("sprites/" + player.getName() + ".png", Texture.class)),
                typeColorMap.get(player.getClass()),
                new MapActorController(level.getMapState(), level.getHighlighter(), player, ACTOR_SIZE)
        );
    }

    public Actor create(Level level, Enemy enemy) {
        return new CharacterActor<Enemy>(
                enemy,
                ACTOR_SIZE,
                level.getWaiter(),
                markerSpriteMap,
                new Sprite(assetManager.get("sprites/" + enemy.getName() + ".png", Texture.class)),
                typeColorMap.get(enemy.getClass()),
                new MapActorController(level.getMapState(), level.getHighlighter(), enemy, ACTOR_SIZE)
        );
    }

    public Actor create(Level level, Terrain terrain) {
        return new BaseActor<Terrain>(
                terrain,
                ACTOR_SIZE,
                level.getWaiter(),
                markerSpriteMap,
                new MapActorController(level.getMapState(), level.getHighlighter(), terrain, ACTOR_SIZE)
        );
    }
}
