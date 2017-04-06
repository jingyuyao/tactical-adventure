package com.jingyuyao.tactical.view.world.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * Attach this component to an {@link com.badlogic.ashley.core.Entity} to signal for removal.
 */
public class Remove implements Component, Poolable {

  @Override
  public void reset() {

  }
}
