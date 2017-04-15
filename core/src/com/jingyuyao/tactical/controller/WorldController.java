package com.jingyuyao.tactical.controller;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.google.common.base.Optional;
import com.jingyuyao.tactical.model.Model;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.World;
import com.jingyuyao.tactical.view.world.WorldModule.WorldViewport;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WorldController extends InputAdapter {

  private final Viewport worldViewport;
  private final WorldCamera worldCamera;
  private final Model model;
  private final World world;
  private final Vector3 vector3 = new Vector3();

  @Inject
  WorldController(
      @WorldViewport Viewport worldViewport,
      WorldCamera worldCamera,
      Model model,
      World world) {
    this.worldViewport = worldViewport;
    this.worldCamera = worldCamera;
    this.model = model;
    this.world = world;
  }

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    return true;
  }

  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    if (!worldCamera.isDragged()) {
      Camera camera = worldViewport.getCamera();
      vector3.set(screenX, screenY, 0);
      camera.unproject(vector3);

      Optional<Cell> cellOptional = world.getCell((int) vector3.x, (int) vector3.y);
      if (cellOptional.isPresent()) {
        model.select(cellOptional.get());
      }
    }
    return true;
  }
}
