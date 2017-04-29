package com.jingyuyao.tactical.controller;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;
import com.google.common.base.Optional;
import com.jingyuyao.tactical.model.Model;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.World;
import com.jingyuyao.tactical.view.world.WorldView;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WorldController extends InputAdapter {

  private final CameraController cameraController;
  private final WorldView worldView;
  private final Model model;
  private final World world;
  private final Vector3 vector3 = new Vector3();

  @Inject
  WorldController(
      CameraController cameraController,
      WorldView worldView,
      Model model,
      World world) {
    this.worldView = worldView;
    this.cameraController = cameraController;
    this.model = model;
    this.world = world;
  }

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    return true;
  }

  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    if (!cameraController.isDragged()) {
      vector3.set(screenX, screenY, 0);
      worldView.unproject(vector3);

      Optional<Cell> cellOptional = world.getCell((int) vector3.x, (int) vector3.y);
      if (cellOptional.isPresent()) {
        model.select(cellOptional.get());
      }
    }
    return true;
  }
}
