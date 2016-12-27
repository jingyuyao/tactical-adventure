package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.jingyuyao.tactical.Assets;
import com.jingyuyao.tactical.controller.MapActorController;
import com.jingyuyao.tactical.model.Level;
import com.jingyuyao.tactical.model.Markers;
import com.jingyuyao.tactical.model.object.Enemy;
import com.jingyuyao.tactical.model.object.Player;
import com.jingyuyao.tactical.model.object.Terrain;

import java.util.HashMap;

/**
 * Creates {@link AbstractActor} from models and adds the proper controllers.
 */
public class ActorFactory {
    public static final float ACTOR_SIZE = 1f; // world units

    private final AssetManager assetManager;
    private final java.util.Map<Markers, Sprite> markerSpriteMap;
    private final java.util.Map<Class, Color> typeColorMap;

    public ActorFactory(AssetManager assetManager) {
        this.assetManager = assetManager;
        markerSpriteMap = new HashMap<Markers, Sprite>();
        markerSpriteMap.put(
                Markers.MOVE,
                new Sprite(assetManager.get(Assets.MOVE_OVERLAY, Texture.class))
        );
        markerSpriteMap.put(
                Markers.DANGER,
                new Sprite(assetManager.get(Assets.DANGER_OVERLAY, Texture.class))
        );
        markerSpriteMap.put(
                Markers.ATTACK,
                new Sprite(assetManager.get(Assets.ATTACK_OVERLAY, Texture.class))
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
                new Sprite(assetManager.get("sprites/" + enemy.getName() + ".png", Texture.class)),
                typeColorMap.get(enemy.getClass()),
                new MapActorController(level.getMapState(), level.getHighlighter(), enemy, ACTOR_SIZE)
        );
    }

    public Actor create(Level level, Terrain terrain) {
        return new TerrainActor(
                terrain,
                ACTOR_SIZE,
                level.getWaiter(),
                markerSpriteMap,
                new MapActorController(level.getMapState(), level.getHighlighter(), terrain, ACTOR_SIZE)
        );
    }
}
