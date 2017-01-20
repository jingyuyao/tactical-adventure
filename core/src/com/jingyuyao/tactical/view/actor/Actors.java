package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.google.common.base.Preconditions;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.controller.MapActorControllerFactory;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.character.event.RemoveCharacter;
import com.jingyuyao.tactical.model.common.ManagedBy;
import com.jingyuyao.tactical.model.event.ClearMap;
import com.jingyuyao.tactical.model.event.NewMap;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.map.Terrain;
import com.jingyuyao.tactical.view.ViewAnnotations.MapViewStage;
import com.jingyuyao.tactical.view.actor.ActorAnnotations.BackingActorMap;
import com.jingyuyao.tactical.view.actor.ActorAnnotations.EnemySprite;
import com.jingyuyao.tactical.view.actor.ActorAnnotations.PlayerSprite;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Actors implements ManagedBy<NewMap, ClearMap> {

  private final Stage stage;
  private final Map<MapObject, MapActor> actorMap;
  private final ActorFactory actorFactory;
  private final MapActorControllerFactory mapActorControllerFactory;
  private final Sprite playerSprite;
  private final Sprite enemySprite;

  @Inject
  Actors(
      EventBus eventBus,
      @MapViewStage Stage stage,
      @BackingActorMap Map<MapObject, MapActor> actorMap,
      ActorFactory actorFactory,
      MapActorControllerFactory mapActorControllerFactory,
      @PlayerSprite Sprite playerSprite,
      @EnemySprite Sprite enemySprite) {
    this.stage = stage;
    this.actorMap = actorMap;
    this.actorFactory = actorFactory;
    this.mapActorControllerFactory = mapActorControllerFactory;
    this.playerSprite = playerSprite;
    this.enemySprite = enemySprite;
    eventBus.register(this);
  }

  @Subscribe
  @Override
  public void initialize(NewMap data) {
    for (Terrain terrain : data.getTerrains()) {
      MapActor actor = actorFactory
          .create(terrain.getCoordinate(), mapActorControllerFactory.create(terrain));
      actorMap.put(terrain, actor);
      stage.addActor(actor);
    }
    // Characters must be added after terrain so they get hit by touch input
    for (Player player : data.getPlayers()) {
      MapActor actor = actorFactory
          .create(player.getCoordinate(), mapActorControllerFactory.create(player), playerSprite,
              Color.WHITE);
      actorMap.put(player, actor);
      stage.addActor(actor);
    }
    for (Enemy enemy : data.getEnemies()) {
      MapActor actor = actorFactory
          .create(enemy.getCoordinate(), mapActorControllerFactory.create(enemy), enemySprite,
              Color.RED);
      actorMap.put(enemy, actor);
      stage.addActor(actor);
    }
  }

  @Subscribe
  @Override
  public void dispose(ClearMap data) {
    actorMap.clear();
    stage.clear();
  }

  @Subscribe
  public void removeCharacter(RemoveCharacter removeCharacter) {
    Character character = removeCharacter.getObject();
    Preconditions.checkState(actorMap.containsKey(character));
    actorMap.get(character).remove();
    actorMap.remove(character);
  }

  MapActor get(MapObject object) {
    return actorMap.get(object);
  }
}
