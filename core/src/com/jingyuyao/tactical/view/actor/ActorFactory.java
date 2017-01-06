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
import com.jingyuyao.tactical.view.ViewConfig;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Creates {@link BaseActor} from models and adds the proper controllers.
 */
@Singleton
public class ActorFactory {

  private final ViewConfig viewConfig;
  private final EventBus eventBus;
  private final Waiter waiter;
  private final MapActorControllerFactory mapActorControllerFactory;
  private final java.util.Map<Marker, Sprite> markerSpriteMap;
  private final java.util.Map<String, Sprite> characterSpriteMap;

  @Inject
  ActorFactory(
      ViewConfig viewConfig,
      EventBus eventBus,
      Waiter waiter,
      MapActorControllerFactory mapActorControllerFactory,
      Map<Marker, Sprite> markerSpriteMap,
      Map<String, Sprite> characterSpriteMap) {
    this.viewConfig = viewConfig;
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
        viewConfig.getActorWorldSize(),
        waiter,
        markerSpriteMap,
        characterSpriteMap.get(player.getName()),
        Color.WHITE,
        mapActorControllerFactory.create(player));
  }

  public Actor create(Enemy enemy) {
    return new CharacterActor<Enemy>(
        eventBus,
        enemy,
        viewConfig.getActorWorldSize(),
        waiter,
        markerSpriteMap,
        characterSpriteMap.get(enemy.getName()),
        Color.RED,
        mapActorControllerFactory.create(enemy));
  }

  public Actor create(Terrain terrain) {
    return new BaseActor<Terrain>(
        eventBus,
        terrain,
        viewConfig.getActorWorldSize(),
        waiter,
        markerSpriteMap,
        mapActorControllerFactory.create(terrain));
  }
}
