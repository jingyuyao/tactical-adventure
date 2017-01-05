package com.jingyuyao.tactical.view;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.BindingAnnotation;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.common.ManagedBy;
import com.jingyuyao.tactical.model.event.ClearMap;
import com.jingyuyao.tactical.model.event.NewMap;
import com.jingyuyao.tactical.model.map.Terrain;
import com.jingyuyao.tactical.view.actor.ActorFactory;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
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

  /**
   * A map view contains a stage with all the actors and a way to render them. The background map is
   * backed by a {@link OrthogonalTiledMapRenderer}.
   *
   * @param stage the stage this view uses
   * @param mapRenderer The tiled map renderer
   */
  @Inject
  MapView(
      EventBus eventBus,
      @MapViewStage Stage stage,
      OrthogonalTiledMapRenderer mapRenderer,
      ActorFactory actorFactory) {
    this.stage = stage;
    this.mapRenderer = mapRenderer;
    this.actorFactory = actorFactory;
    eventBus.register(this);
  }

  @Subscribe
  @Override
  public void initialize(NewMap data) {
    for (Terrain terrain : data.getTerrains()) {
      stage.addActor(actorFactory.create(terrain));
    }
    // Characters must be added after terrain so they get hit by touch input
    for (Player player : data.getPlayers()) {
      stage.addActor(actorFactory.create(player));
    }
    for (Enemy enemy : data.getEnemies()) {
      stage.addActor(actorFactory.create(enemy));
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

  @BindingAnnotation
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  public @interface MapViewStage {

  }
}
