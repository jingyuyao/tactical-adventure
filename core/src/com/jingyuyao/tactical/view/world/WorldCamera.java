package com.jingyuyao.tactical.view.world;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.ModelBusListener;
import com.jingyuyao.tactical.model.event.WorldLoaded;
import com.jingyuyao.tactical.model.event.WorldReset;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.ship.ShipGroup;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.World;
import com.jingyuyao.tactical.view.world.WorldViewModule.WorldViewport;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@ModelBusListener
public class WorldCamera {

  private final ExtendViewport viewport;
  private final Random random = new Random();
  private int worldWidth = 0;
  private int worldHeight = 0;

  @Inject
  WorldCamera(@WorldViewport ExtendViewport viewport) {
    this.viewport = viewport;
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
   * Set the camera world position to (x,y) or the closest point within camera's bounds.
   */
  public void setCameraPosition(float x, float y) {
    float lowerXBound = getViewportWorldWidth() / 2f;
    float upperXBound = worldWidth - lowerXBound;
    float lowerYBound = getViewportWorldHeight() / 2f;
    float upperYBound = worldHeight - lowerYBound;

    Vector3 position = viewport.getCamera().position;
    position.x = bound(lowerXBound, x, upperXBound);
    position.y = bound(lowerYBound, y, upperYBound);
  }

  /**
   * Move the camera world position by (deltaX,deltaY). Camera will stay in bounds.
   */
  public void moveCameraBy(float deltaX, float deltaY) {
    Vector3 position = viewport.getCamera().position;
    setCameraPosition(position.x + deltaX, position.y + deltaY);
  }

  void apply() {
    viewport.apply();
  }

  void resize(int width, int height) {
    viewport.update(width, height);
  }

  Matrix4 getProjectionMatrix() {
    return viewport.getCamera().combined;
  }

  @Subscribe
  void worldLoaded(WorldLoaded worldLoaded) {
    World world = worldLoaded.getWorld();
    worldWidth = world.getMaxWidth();
    worldHeight = world.getMaxHeight();

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
      setCameraPosition(unboundedPlayerX, unboundedPlayerY);
    }
  }

  @Subscribe
  void worldReset(WorldReset worldReset) {
    worldWidth = 0;
    worldHeight = 0;
  }

  private static float bound(float min, float value, float max) {
    return Math.max(min, Math.min(value, max));
  }
}
