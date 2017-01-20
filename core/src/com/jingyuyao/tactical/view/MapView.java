package com.jingyuyao.tactical.view;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.jingyuyao.tactical.view.ViewAnnotations.MapViewStage;
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

  /**
   * A map view contains a stage with all the actors and a way to render them. The background map is
   * backed by a {@link OrthogonalTiledMapRenderer}.
   */
  @Inject
  MapView(@MapViewStage Stage stage, OrthogonalTiledMapRenderer mapRenderer) {
    this.stage = stage;
    this.mapRenderer = mapRenderer;
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
