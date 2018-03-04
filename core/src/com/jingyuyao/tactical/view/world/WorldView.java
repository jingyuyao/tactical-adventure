package com.jingyuyao.tactical.view.world;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.ModelBusListener;
import com.jingyuyao.tactical.model.event.WorldLoaded;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.ship.ShipGroup;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.World;
import com.jingyuyao.tactical.view.world.WorldViewModule.WorldEngine;
import com.jingyuyao.tactical.view.world.WorldViewModule.WorldViewport;
import com.jingyuyao.tactical.view.world.system.Systems;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@ModelBusListener
public class WorldView {

  private final Batch batch;
  private final ExtendViewport viewport;
  private final PooledEngine engine;
  private final Random random = new Random();

  @Inject
  WorldView(
      Batch batch,
      @WorldViewport ExtendViewport viewport,
      @WorldEngine PooledEngine engine,
      Systems systems) {
    this.batch = batch;
    this.viewport = viewport;
    this.engine = engine;
    systems.addTo(engine);
  }

  public void update(float delta) {
    // tell openGL to use this viewport and calls camera.update()
    viewport.apply();
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
    return viewport.getMinWorldWidth();
  }

  public float getViewportWorldHeight() {
    return viewport.getMinWorldHeight();
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

  @Subscribe
  void worldLoaded(WorldLoaded worldLoaded) {
    World world = worldLoaded.getWorld();
    List<Cell> playerCells = new ArrayList<>();
    for (Cell cell : world.getWorldCells()) {
      for (Ship ship : cell.ship().asSet()) {
        if (ship.inGroup(ShipGroup.PLAYER)) {
          playerCells.add(cell);
        }
      }
    }

    // Hum...
    if (playerCells.isEmpty()) {
      setCameraPosition(world.getMaxWidth() / 2f, world.getMaxHeight() / 2f);
    } else {
      Cell cell = playerCells.get(random.nextInt(playerCells.size()));

      float unboundedPlayerX = cell.getCoordinate().getX();
      float unboundedPlayerY = cell.getCoordinate().getY();
      float lowerXBound = getViewportWorldWidth() / 2f;
      float upperXBound = world.getMaxWidth() - lowerXBound;
      float lowerYBound = getViewportWorldHeight() / 2f;
      float upperYBound = world.getMaxHeight() - lowerYBound;

      float boundedPlayerX = bound(lowerXBound, unboundedPlayerX, upperXBound);
      float boundedPlayerY = bound(lowerYBound, unboundedPlayerY, upperYBound);
      setCameraPosition(boundedPlayerX, boundedPlayerY);
    }
  }

  private float bound(float min, float value, float max) {
    return Math.max(min, Math.min(value, max));
  }
}
