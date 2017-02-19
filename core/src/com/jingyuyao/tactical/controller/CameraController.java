package com.jingyuyao.tactical.controller;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jingyuyao.tactical.model.map.Terrains;
import com.jingyuyao.tactical.view.actor.WorldModule.MapActorsViewport;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class CameraController extends InputAdapter {

  private static final float DRAG_SCREEN_DISTANCE_CUTOFF = 10f;

  private final Viewport viewport;
  private final Terrains terrains;
  private final Vector2 initialTouch = new Vector2();
  private final Vector2 lastTouch = new Vector2();
  private int lastPointer = -1;
  private boolean dragged = false;

  @Inject
  CameraController(@MapActorsViewport Viewport viewport, Terrains terrains) {
    this.viewport = viewport;
    this.terrains = terrains;
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
      dragged = initialTouch.dst(lastTouch) > DRAG_SCREEN_DISTANCE_CUTOFF;
    }
  }

  private float bound(float min, float value, float max) {
    return Math.max(min, Math.min(value, max));
  }
}
