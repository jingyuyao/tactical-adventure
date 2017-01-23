package com.jingyuyao.tactical.controller;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.common.EventSubscriber;
import com.jingyuyao.tactical.model.event.ClearMap;
import com.jingyuyao.tactical.model.event.NewMap;
import com.jingyuyao.tactical.view.ViewAnnotations.MapViewViewport;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class DragCameraController extends InputAdapter implements EventSubscriber {

  private final Viewport viewport;
  private int worldWidth;
  private int worldHeight;
  private int lastPointer = -1;
  private int lastX;
  private int lastY;

  @Inject
  DragCameraController(@MapViewViewport Viewport viewport) {
    this.viewport = viewport;
  }

  @Subscribe
  public void initialize(NewMap data) {
    worldWidth = data.getWidth();
    worldHeight = data.getHeight();
  }

  @Subscribe
  public void dispose(ClearMap data) {
    lastPointer = -1;
    lastX = 0;
    lastY = 0;
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
      float horizontalDragScale = (float) worldWidth / screenWidth;
      float verticalDragScale = (float) worldHeight / screenHeight;

      float deltaX = (screenX - lastX) * horizontalDragScale;
      float deltaY = (screenY - lastY) * verticalDragScale;

      Camera camera = viewport.getCamera();
      Vector3 cameraPosition = camera.position;

      // world is y-up, screen is y-down
      float rawNewX = cameraPosition.x - deltaX;
      float rawNewY = cameraPosition.y + deltaY;

      float cameraLowerXBound = viewport.getWorldWidth() / 2f;
      float cameraUpperXBound = worldWidth - cameraLowerXBound;
      float cameraLowerYBound = viewport.getWorldHeight() / 2f;
      float cameraUpperYBound = worldHeight - cameraLowerYBound;

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
