package com.jingyuyao.tactical.view.world;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.controller.WorldCamera;
import com.jingyuyao.tactical.controller.WorldController;
import com.jingyuyao.tactical.view.world.WorldModule.WorldViewport;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WorldView {

  private final WorldEngine worldEngine;
  private final OrthogonalTiledMapRenderer mapRenderer;
  private final Viewport viewport;
  private final WorldCamera worldCamera;
  private final InputProcessor inputProcessor;

  @Inject
  WorldView(
      WorldEngine worldEngine,
      OrthogonalTiledMapRenderer mapRenderer,
      @WorldViewport Viewport viewport,
      WorldCamera worldCamera,
      WorldController worldController) {
    this.worldEngine = worldEngine;
    this.mapRenderer = mapRenderer;
    this.viewport = viewport;
    this.worldCamera = worldCamera;
    this.inputProcessor = new InputMultiplexer(worldCamera, worldController);
  }

  public void register(EventBus eventBus) {
    worldEngine.register(eventBus);
  }

  public InputProcessor getInputProcessor() {
    return inputProcessor;
  }

  public void center() {
    worldCamera.center();
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
