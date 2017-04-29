package com.jingyuyao.tactical.view.world;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jingyuyao.tactical.view.world.WorldModule.WorldViewport;
import com.jingyuyao.tactical.view.world.system.SystemModule.EntitySystems;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WorldView {

  private final Batch batch;
  private final Viewport viewport;
  private final OrthogonalTiledMapRenderer mapRenderer;
  private final Engine engine;

  @Inject
  WorldView(
      Batch batch,
      @WorldViewport Viewport viewport,
      OrthogonalTiledMapRenderer mapRenderer,
      Engine engine,
      @EntitySystems List<EntitySystem> entitySystems) {
    this.batch = batch;
    this.viewport = viewport;
    this.mapRenderer = mapRenderer;
    this.engine = engine;
    for (EntitySystem system : entitySystems) {
      engine.addSystem(system);
    }
  }

  public void update(float delta) {
    // tell openGL to use this viewport and calls camera.update()
    viewport.apply();
    // Render terrains
    mapRenderer.setView((OrthographicCamera) viewport.getCamera());
    mapRenderer.render();
    // Render and update everything else
    batch.begin();
    batch.setProjectionMatrix(viewport.getCamera().combined);
    engine.update(delta);
    batch.end();
  }

  public void resize(int width, int height) {
    viewport.update(width, height);
  }

  public float getViewportWorldWidth() {
    return viewport.getWorldWidth();
  }

  public float getViewportWorldHeight() {
    return viewport.getWorldHeight();
  }

  public int getViewportScreenWidth() {
    return viewport.getScreenWidth();
  }

  public int getViewportScreenHeight() {
    return viewport.getScreenHeight();
  }

  /**
   * See {@link com.badlogic.gdx.graphics.Camera#unproject(Vector3)}
   */
  public void unproject(Vector3 screenCoords) {
    viewport.getCamera().unproject(screenCoords);
  }

  /**
   * Returns the world position for the camera. Do not modify the return value.
   * Use {@link #setCameraPosition(float, float)} to change camera position.
   */
  public Vector3 getCameraPosition() {
    return viewport.getCamera().position;
  }

  /**
   * Set the camera world position to (x,y).
   */
  public void setCameraPosition(float x, float y) {
    Vector3 position = viewport.getCamera().position;
    position.x = x;
    position.y = y;
  }
}
