package com.jingyuyao.tactical.controller;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jingyuyao.tactical.model.map.Terrains;
import com.jingyuyao.tactical.view.ViewModule.MapViewViewport;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class DragCameraController extends InputAdapter {

  private final Viewport viewport;
  private final Terrains terrains;
  private final Vector2 lastTouch = new Vector2();
  private int lastPointer = -1;

  @Inject
  DragCameraController(@MapViewViewport Viewport viewport, Terrains terrains) {
    this.viewport = viewport;
    this.terrains = terrains;
  }

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    lastPointer = pointer;
    lastTouch.set(screenX, screenY);
    return false;
  }

  @Override
  public boolean touchDragged(int screenX, int screenY, int pointer) {
    if (lastPointer == pointer) {
      float horizontalScale = viewport.getWorldWidth() / viewport.getScreenWidth();
      float verticalScale = viewport.getWorldHeight() / viewport.getScreenHeight();

      float deltaWorldX = (screenX - lastTouch.x) * horizontalScale;
      float deltaWorldY = (screenY - lastTouch.y) * verticalScale;

      Camera camera = viewport.getCamera();
      Vector3 cameraPosition = camera.position;

      // world is y-up, screen is y-down
      float unboundedNewWorldX = cameraPosition.x - deltaWorldX;
      float unboundedNewWorldY = cameraPosition.y + deltaWorldY;

      float lowerXBound = viewport.getWorldWidth() / 2f;
      float upperXBound = terrains.getWidth() - lowerXBound;
      float lowerYBound = viewport.getWorldHeight() / 2f;
      float upperYBound = terrains.getHeight() - lowerYBound;

      float boundedNewWorldX = bound(lowerXBound, unboundedNewWorldX, upperXBound);
      float boundedNewWorldY = bound(lowerYBound, unboundedNewWorldY, upperYBound);

      cameraPosition.x = boundedNewWorldX;
      cameraPosition.y = boundedNewWorldY;
      camera.update();

      lastTouch.set(screenX, screenY);
    }
    return false;
  }

  private float bound(float min, float value, float max) {
    return Math.max(min, Math.min(value, max));
  }
}
