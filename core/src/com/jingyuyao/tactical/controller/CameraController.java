package com.jingyuyao.tactical.controller;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.jingyuyao.tactical.view.world.WorldView;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CameraController extends InputAdapter {

  private final ControllerConfig controllerConfig;
  private final WorldView worldView;
  private final Vector2 initialTouch = new Vector2();
  private final Vector2 lastTouch = new Vector2();
  private int lastPointer = -1;
  private boolean dragged = false;

  @Inject
  CameraController(ControllerConfig controllerConfig, WorldView worldView) {
    this.controllerConfig = controllerConfig;
    this.worldView = worldView;
  }

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    lastPointer = pointer;
    initialTouch.set(screenX, screenY);
    lastTouch.set(screenX, screenY);
    dragged = false;
    return false;
  }

  @Override
  public boolean touchDragged(int screenX, int screenY, int pointer) {
    if (lastPointer == pointer) {
      float viewportWorldWidth = worldView.getViewportWorldWidth();
      float viewportWorldHeight = worldView.getViewportWorldHeight();
      float horizontalScale = viewportWorldWidth / worldView.getViewportScreenWidth();
      float verticalScale = viewportWorldHeight / worldView.getViewportScreenHeight();
      float deltaWorldX = (screenX - lastTouch.x) * horizontalScale;
      float deltaWorldY = (screenY - lastTouch.y) * verticalScale;

      // world is y-up, screen is y-down
      worldView.moveCameraBy(-deltaWorldX, deltaWorldY);
      lastTouch.set(screenX, screenY);
      calcDragged();
    }
    return false;
  }

  boolean isDragged() {
    return dragged;
  }

  private void calcDragged() {
    // drag detection must be handled on a InputAdapter level rather than InputListener level
    // since our world moves along with the touch so InputListener always think it is being clicked
    // once the touch has moved a certain distanced away it is considered dragged even if it moves
    // back to the original position.
    if (!dragged) {
      dragged = initialTouch.dst(lastTouch) > controllerConfig.getDragScreenCutoff();
    }
  }
}
