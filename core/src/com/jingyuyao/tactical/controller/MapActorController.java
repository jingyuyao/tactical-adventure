package com.jingyuyao.tactical.controller;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.state.MapState;
import javax.inject.Inject;

public class MapActorController extends InputListener {

  private final CameraController cameraController;
  private final MapState mapState;
  private final MapObject object;

  @Inject
  MapActorController(
      MapState mapState, CameraController cameraController, @Assisted MapObject object) {
    this.cameraController = cameraController;
    this.mapState = mapState;
    this.object = object;
  }

  @Override
  public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
    return true;
  }

  @Override
  public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
    if (!cameraController.isDragged()) {
      object.select(mapState);
    }
  }
}
