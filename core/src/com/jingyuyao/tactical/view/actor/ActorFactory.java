package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.controller.MapActorController;
import com.jingyuyao.tactical.model.Highlighter;
import com.jingyuyao.tactical.model.Marker;
import com.jingyuyao.tactical.model.Waiter;
import com.jingyuyao.tactical.model.object.Enemy;
import com.jingyuyao.tactical.model.object.Player;
import com.jingyuyao.tactical.model.object.Terrain;
import com.jingyuyao.tactical.model.state.MapState;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Creates {@link BaseActor} from models and adds the proper controllers.
 */
@Singleton
public class ActorFactory {
    private static final float ACTOR_SIZE = 1f; // world units

    private final EventBus eventBus;
    private final Waiter waiter;
    private final MapState mapState;
    private final Highlighter highlighter;
    private final java.util.Map<Marker, Sprite> markerSpriteMap;
    private final java.util.Map<String, Sprite> characterSpriteMap;

    @Inject
    public ActorFactory(
            EventBus eventBus,
            Waiter waiter,
            MapState mapState,
            Highlighter highlighter,
            java.util.Map<Marker, Sprite> markerSpriteMap,
            java.util.Map<String, Sprite> characterSpriteMap) {
        this.eventBus = eventBus;
        this.waiter = waiter;
        this.mapState = mapState;
        this.highlighter = highlighter;
        this.markerSpriteMap = markerSpriteMap;
        this.characterSpriteMap = characterSpriteMap;
    }

    public Actor create(Player player) {
        return new PlayerActor(
                eventBus,
                player,
                ACTOR_SIZE,
                waiter,
                markerSpriteMap,
                characterSpriteMap.get(player.getName()),
                Color.WHITE,
                new MapActorController(mapState, highlighter, player, ACTOR_SIZE)
        );
    }

    public Actor create(Enemy enemy) {
        return new CharacterActor<Enemy>(
                eventBus,
                enemy,
                ACTOR_SIZE,
                waiter,
                markerSpriteMap,
                characterSpriteMap.get(enemy.getName()),
                Color.RED,
                new MapActorController(mapState, highlighter, enemy, ACTOR_SIZE)
        );
    }

    public Actor create(Terrain terrain) {
        return new BaseActor<Terrain>(
                eventBus,
                terrain,
                ACTOR_SIZE,
                waiter,
                markerSpriteMap,
                new MapActorController(mapState, highlighter, terrain, ACTOR_SIZE)
        );
    }
}
