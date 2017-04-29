package com.jingyuyao.tactical.controller;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jingyuyao.tactical.model.world.World;
import com.jingyuyao.tactical.view.world.WorldModule.WorldViewport;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CameraController extends InputAdapter {

  private final ControllerConfig controllerConfig;
  private final Viewport worldViewport;
  private final World world;
  private final Vector2 initialTouch = new Vector2();
  private final Vector2 lastTouch = new Vector2();
  private int lastPointer = -1;
  private boolean dragged = false;

  @Inject
  CameraController(
      ControllerConfig controllerConfig, @WorldViewport Viewport worldViewport, World world) {
    this.controllerConfig = controllerConfig;
    this.worldViewport = worldViewport;
    this.world = world;
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
      float horizontalScale = worldViewport.getWorldWidth() / worldViewport.getScreenWidth();
      float verticalScale = worldViewport.getWorldHeight() / worldViewport.getScreenHeight();

      float deltaWorldX = (screenX - lastTouch.x) * horizontalScale;
      float deltaWorldY = (screenY - lastTouch.y) * verticalScale;

      Camera camera = worldViewport.getCamera();
      Vector3 cameraPosition = camera.position;

      // world is y-up, screen is y-down
      float unboundedNewWorldX = cameraPosition.x - deltaWorldX;
      float unboundedNewWorldY = cameraPosition.y + deltaWorldY;

      float lowerXBound = worldViewport.getWorldWidth() / 2f;
      float upperXBound = world.getMaxWidth() - lowerXBound;
      float lowerYBound = worldViewport.getWorldHeight() / 2f;
      float upperYBound = world.getMaxHeight() - lowerYBound;

      float boundedNewWorldX = bound(lowerXBound, unboundedNewWorldX, upperXBound);
      float boundedNewWorldY = bound(lowerYBound, unboundedNewWorldY, upperYBound);

      cameraPosition.x = boundedNewWorldX;
      cameraPosition.y = boundedNewWorldY;
      camera.update();

      lastTouch.set(screenX, screenY);
      calcDragged();
    }
    return false;
  }

  public void center() {
    Camera camera = worldViewport.getCamera();
    camera.position.x = world.getMaxWidth() / 2f;
    camera.position.y = world.getMaxHeight() / 2f;
    camera.update();
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

  private float bound(float min, float value, float max) {
    return Math.max(min, Math.min(value, max));
  }
}
