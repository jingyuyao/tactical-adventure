package com.jingyuyao.tactical.view;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.controller.ControllerFactory;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.map.Terrain;
import com.jingyuyao.tactical.model.map.event.AddEnemy;
import com.jingyuyao.tactical.model.map.event.AddPlayer;
import com.jingyuyao.tactical.model.map.event.AddTerrain;
import com.jingyuyao.tactical.view.ViewAnnotations.MapViewStage;
import com.jingyuyao.tactical.view.actor.ActorFactory;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Contains and renders the stage. The stage is rendered in a grid scale (i.e. showing a 30x20
 * grid).
 */
@Singleton
public class MapView {

  private final Stage stage;
  private final OrthogonalTiledMapRenderer mapRenderer;
  private final ActorFactory actorFactory;
  private final ControllerFactory controllerFactory;

  /**
   * A map view contains a stage with all the actors and a way to render them. The background map is
   * backed by a {@link OrthogonalTiledMapRenderer}.
   */
  @Inject
  MapView(@MapViewStage Stage stage,
      OrthogonalTiledMapRenderer mapRenderer,
      ActorFactory actorFactory,
      ControllerFactory controllerFactory) {
    this.stage = stage;
    this.mapRenderer = mapRenderer;
    this.actorFactory = actorFactory;
    this.controllerFactory = controllerFactory;
  }

  @Subscribe
  public void addTerrain(AddTerrain addTerrain) {
    Terrain terrain = addTerrain.getObject();
    stage.addActor(actorFactory.create(terrain, controllerFactory.create(terrain)));
  }

  @Subscribe
  public void addPlayer(AddPlayer addPlayer) {
    Player player = addPlayer.getObject();
    stage.addActor(actorFactory.create(player, controllerFactory.create(player)));
  }

  @Subscribe
  public void addEnemy(AddEnemy addEnemy) {
    Enemy enemy = addEnemy.getObject();
    stage.addActor(actorFactory.create(enemy, controllerFactory.create(enemy)));
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
