package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.controller.ControllerFactory;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.common.EventSubscriber;
import com.jingyuyao.tactical.model.event.ClearMap;
import com.jingyuyao.tactical.model.event.NewMap;
import com.jingyuyao.tactical.model.map.Terrain;
import com.jingyuyao.tactical.view.ViewAnnotations.MapViewStage;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Actors implements EventSubscriber {

  private final Stage stage;
  private final ActorFactory actorFactory;
  private final ControllerFactory controllerFactory;

  @Inject
  Actors(
      @MapViewStage Stage stage,
      ActorFactory actorFactory,
      ControllerFactory controllerFactory) {
    this.stage = stage;
    this.actorFactory = actorFactory;
    this.controllerFactory = controllerFactory;
  }

  @Subscribe
  public void initialize(NewMap data) {
    for (Terrain terrain : data.getTerrains()) {
      stage.addActor(actorFactory.create(terrain, controllerFactory.create(terrain)));
    }
    // Characters must be added after terrain so they get hit by touch input
    for (Player player : data.getPlayers()) {
      stage.addActor(actorFactory.create(player, controllerFactory.create(player)));
    }
    for (Enemy enemy : data.getEnemies()) {
      stage.addActor(actorFactory.create(enemy, controllerFactory.create(enemy)));
    }
  }

  @Subscribe
  public void dispose(ClearMap data) {
    stage.clear();
  }
}
