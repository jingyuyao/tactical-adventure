package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.controller.MapActorControllerFactory;
import com.jingyuyao.tactical.model.Waiter;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.map.Terrain;
import com.jingyuyao.tactical.model.mark.Marker;
import java.util.Map;
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
  private final MapActorControllerFactory mapActorControllerFactory;
  private final java.util.Map<Marker, Sprite> markerSpriteMap;
  private final java.util.Map<String, Sprite> characterSpriteMap;

  @Inject
  ActorFactory(
      EventBus eventBus,
      Waiter waiter,
      MapActorControllerFactory mapActorControllerFactory,
      Map<Marker, Sprite> markerSpriteMap,
      Map<String, Sprite> characterSpriteMap) {
    this.eventBus = eventBus;
    this.waiter = waiter;
    this.mapActorControllerFactory = mapActorControllerFactory;
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
        mapActorControllerFactory.create(player, ACTOR_SIZE));
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
        mapActorControllerFactory.create(enemy, ACTOR_SIZE));
  }

  public Actor create(Terrain terrain) {
    return new BaseActor<Terrain>(
        eventBus,
        terrain,
        ACTOR_SIZE,
        waiter,
        markerSpriteMap,
        mapActorControllerFactory.create(terrain, ACTOR_SIZE));
  }
}
