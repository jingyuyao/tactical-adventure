package com.jingyuyao.tactical.view.world2;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jingyuyao.tactical.view.world2.WorldModule.WorldViewport;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WorldView {

  private final Entities entities;
  private final OrthogonalTiledMapRenderer mapRenderer;
  private final Viewport viewport;

  @Inject
  WorldView(
      Entities entities,
      OrthogonalTiledMapRenderer mapRenderer,
      @WorldViewport Viewport viewport) {
    this.entities = entities;
    this.mapRenderer = mapRenderer;
    this.viewport = viewport;
    // TODO: fix initial camera position
  }

  public void update(float delta) {
    viewport.apply();
    mapRenderer.setView((OrthographicCamera) viewport.getCamera());
    mapRenderer.render();
    entities.update(delta);
  }

  public void resize(int width, int height) {
    viewport.update(width, height);
  }
}
