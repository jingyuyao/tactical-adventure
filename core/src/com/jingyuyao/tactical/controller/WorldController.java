package com.jingyuyao.tactical.controller;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;
import com.google.common.base.Optional;
import com.jingyuyao.tactical.model.state.WorldState;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.World;
import com.jingyuyao.tactical.view.world.WorldCamera;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WorldController extends InputAdapter {

  private final CameraController cameraController;
  private final WorldCamera worldCamera;
  private final World world;
  private final WorldState worldState;
  private final Vector3 vector3 = new Vector3();

  @Inject
  WorldController(
      CameraController cameraController,
      WorldCamera worldCamera,
      World world,
      WorldState worldState) {
    this.worldCamera = worldCamera;
    this.cameraController = cameraController;
    this.world = world;
    this.worldState = worldState;
  }

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    return true;
  }

  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    if (!cameraController.isDragged()) {
      vector3.set(screenX, screenY, 0);
      worldCamera.unproject(vector3);

      Optional<Cell> cellOptional = world.cell((int) vector3.x, (int) vector3.y);
      if (cellOptional.isPresent()) {
        worldState.select(cellOptional.get());
      }
    }
    return true;
  }
}
