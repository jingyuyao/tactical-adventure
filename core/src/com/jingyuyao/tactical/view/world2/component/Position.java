package com.jingyuyao.tactical.view.world2.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Position implements Component, Poolable {

  private float x = -1f;
  private float y = -1f;

  public float getX() {
    return x;
  }

  public void setX(float x) {
    this.x = x;
  }

  public float getY() {
    return y;
  }

  public void setY(float y) {
    this.y = y;
  }

  @Override
  public void reset() {
    x = -1f;
    y = -1f;
  }
}
