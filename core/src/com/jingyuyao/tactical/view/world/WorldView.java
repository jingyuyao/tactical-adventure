package com.jingyuyao.tactical.view.world;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jingyuyao.tactical.controller.CameraController;
import com.jingyuyao.tactical.controller.WorldController;
import com.jingyuyao.tactical.view.world.WorldModule.WorldViewport;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WorldView {

  private final WorldEngine worldEngine;
  private final OrthogonalTiledMapRenderer mapRenderer;
  private final Viewport viewport;
  private final CameraController cameraController;
  private final InputProcessor inputProcessor;

  @Inject
  WorldView(
      WorldEngine worldEngine,
      OrthogonalTiledMapRenderer mapRenderer,
      @WorldViewport Viewport viewport,
      CameraController cameraController,
      WorldController worldController) {
    this.worldEngine = worldEngine;
    this.mapRenderer = mapRenderer;
    this.viewport = viewport;
    this.cameraController = cameraController;
    this.inputProcessor = new InputMultiplexer(cameraController, worldController);
  }

  public InputProcessor getInputProcessor() {
    return inputProcessor;
  }

  public void center() {
    cameraController.center();
  }

  public void update(float delta) {
    viewport.apply();
    mapRenderer.setView((OrthographicCamera) viewport.getCamera());
    mapRenderer.render();
    worldEngine.update(delta);
  }

  public void resize(int width, int height) {
    viewport.update(width, height);
  }
}
