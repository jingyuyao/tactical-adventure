package com.jingyuyao.tactical.view.world;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jingyuyao.tactical.view.world.WorldModule.WorldViewport;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WorldView {

  private final ViewEngine viewEngine;
  private final OrthogonalTiledMapRenderer mapRenderer;
  private final Viewport viewport;

  @Inject
  WorldView(
      ViewEngine viewEngine,
      OrthogonalTiledMapRenderer mapRenderer,
      @WorldViewport Viewport viewport) {
    this.viewEngine = viewEngine;
    this.mapRenderer = mapRenderer;
    this.viewport = viewport;
  }

  public void update(float delta) {
    viewport.apply();
    mapRenderer.setView((OrthographicCamera) viewport.getCamera());
    mapRenderer.render();
    viewEngine.update(delta);
  }

  public void resize(int width, int height) {
    viewport.update(width, height);
  }
}
