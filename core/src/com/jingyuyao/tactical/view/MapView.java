package com.jingyuyao.tactical.view;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.controller.MapActorControllerFactory;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.common.ManagedBy;
import com.jingyuyao.tactical.model.event.ClearMap;
import com.jingyuyao.tactical.model.event.NewMap;
import com.jingyuyao.tactical.model.map.Terrain;
import com.jingyuyao.tactical.view.ViewAnnotations.MapViewStage;
import com.jingyuyao.tactical.view.actor.ActorFactory;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Contains and renders the stage. The stage is rendered in a grid scale (i.e. showing a 30x20
 * grid).
 */
@Singleton
public class MapView implements ManagedBy<NewMap, ClearMap> {

  private final Stage stage;
  private final OrthogonalTiledMapRenderer mapRenderer;
  private final ActorFactory actorFactory;
  private final MapActorControllerFactory mapActorControllerFactory;

  /**
   * A map view contains a stage with all the actors and a way to render them. The background map is
   * backed by a {@link OrthogonalTiledMapRenderer}.
   */
  @Inject
  MapView(
      EventBus eventBus,
      @MapViewStage Stage stage,
      OrthogonalTiledMapRenderer mapRenderer,
      ActorFactory actorFactory,
      MapActorControllerFactory mapActorControllerFactory) {
    this.stage = stage;
    this.mapRenderer = mapRenderer;
    this.actorFactory = actorFactory;
    this.mapActorControllerFactory = mapActorControllerFactory;
    eventBus.register(this);
  }

  @Subscribe
  @Override
  public void initialize(NewMap data) {
    for (Terrain terrain : data.getTerrainGrid()) {
      stage.addActor(
          actorFactory.createTerrainActor(terrain, mapActorControllerFactory.create(terrain)));
    }
    // Characters must be added after terrain so they get hit by touch input
    for (Player player : data.getPlayers()) {
      stage.addActor(
          actorFactory.createPlayerActor(player, mapActorControllerFactory.create(player)));
    }
    for (Enemy enemy : data.getEnemies()) {
      stage.addActor(actorFactory.createEnemyActor(enemy, mapActorControllerFactory.create(enemy)));
    }
  }

  @Subscribe
  @Override
  public void dispose(ClearMap data) {
    stage.clear();
  }

  void act(float delta) {
    stage.act(delta);
  }

  void draw() {
    stage.getViewport().apply();
    mapRenderer.setView((OrthographicCamera) stage.getCamera());
    mapRenderer.render();
    stage.draw();
  }

  void resize(int width, int height) {
    // TODO: update camera so we don't show black bars
    stage.getViewport().update(width, height);
  }

  void dispose() {
    stage.dispose();
  }
}
