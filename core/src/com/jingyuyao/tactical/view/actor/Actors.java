package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.controller.ControllerFactory;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.character.event.RemoveCharacter;
import com.jingyuyao.tactical.model.common.EventSubscriber;
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
public class Actors implements EventSubscriber {

  private final Stage stage;
  private final Map<MapObject, MapActor> actorMap;
  private final ActorFactory actorFactory;
  private final ControllerFactory controllerFactory;
  private final Sprite playerSprite;
  private final Sprite enemySprite;

  @Inject
  Actors(
      @MapViewStage Stage stage,
      @BackingActorMap Map<MapObject, MapActor> actorMap,
      ActorFactory actorFactory,
      ControllerFactory controllerFactory,
      @PlayerSprite Sprite playerSprite,
      @EnemySprite Sprite enemySprite) {
    this.stage = stage;
    this.actorMap = actorMap;
    this.actorFactory = actorFactory;
    this.controllerFactory = controllerFactory;
    this.playerSprite = playerSprite;
    this.enemySprite = enemySprite;
  }

  @Subscribe
  public void initialize(NewMap data) {
    for (Terrain terrain : data.getTerrains()) {
      MapActor actor = actorFactory
          .create(terrain.getCoordinate(), controllerFactory.create(terrain));
      actorMap.put(terrain, actor);
      stage.addActor(actor);
    }
    // Characters must be added after terrain so they get hit by touch input
    for (Player player : data.getPlayers()) {
      MapActor actor = actorFactory
          .create(player.getCoordinate(), controllerFactory.create(player), playerSprite,
              Color.WHITE);
      actorMap.put(player, actor);
      stage.addActor(actor);
    }
    for (Enemy enemy : data.getEnemies()) {
      MapActor actor = actorFactory
          .create(enemy.getCoordinate(), controllerFactory.create(enemy), enemySprite,
              Color.RED);
      actorMap.put(enemy, actor);
      stage.addActor(actor);
    }
  }

  @Subscribe
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

  Iterable<MapActor> getAll(Iterable<MapObject> objects) {
    return Iterables.transform(objects, new Function<MapObject, MapActor>() {
      @Override
      public MapActor apply(MapObject input) {
        return get(input);
      }
    });
  }
}
