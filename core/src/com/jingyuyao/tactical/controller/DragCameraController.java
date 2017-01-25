package com.jingyuyao.tactical.controller;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jingyuyao.tactical.model.map.Terrains;
import com.jingyuyao.tactical.view.ViewAnnotations.MapViewViewport;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class DragCameraController extends InputAdapter {

  private final Viewport viewport;
  private final Terrains terrains;
  private int lastPointer = -1;
  private int lastX;
  private int lastY;

  @Inject
  DragCameraController(@MapViewViewport Viewport viewport, Terrains terrains) {
    this.viewport = viewport;
    this.terrains = terrains;
  }

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    lastPointer = pointer;
    lastX = screenX;
    lastY = screenY;
    return false;
  }

  @Override
  public boolean touchDragged(int screenX, int screenY, int pointer) {
    if (lastPointer == pointer) {
      int screenWidth = viewport.getScreenWidth();
      int screenHeight = viewport.getScreenHeight();
      float horizontalDragScale = (float) terrains.getWidth() / screenWidth;
      float verticalDragScale = (float) terrains.getHeight() / screenHeight;

      float deltaX = (screenX - lastX) * horizontalDragScale;
      float deltaY = (screenY - lastY) * verticalDragScale;

      Camera camera = viewport.getCamera();
      Vector3 cameraPosition = camera.position;

      // world is y-up, screen is y-down
      float rawNewX = cameraPosition.x - deltaX;
      float rawNewY = cameraPosition.y + deltaY;

      float cameraLowerXBound = viewport.getWorldWidth() / 2f;
      float cameraUpperXBound = terrains.getWidth() - cameraLowerXBound;
      float cameraLowerYBound = viewport.getWorldHeight() / 2f;
      float cameraUpperYBound = terrains.getHeight() - cameraLowerYBound;

      float boundedNewX = bound(cameraLowerXBound, rawNewX, cameraUpperXBound);
      float boundedNewY = bound(cameraLowerYBound, rawNewY, cameraUpperYBound);

      cameraPosition.x = boundedNewX;
      cameraPosition.y = boundedNewY;
      camera.update();

      lastX = screenX;
      lastY = screenY;
    }
    return false;
  }

  private float bound(float min, float value, float max) {
    return Math.max(min, Math.min(value, max));
  }
}
