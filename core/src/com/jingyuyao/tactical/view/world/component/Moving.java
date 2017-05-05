package com.jingyuyao.tactical.view.world.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.jingyuyao.tactical.model.event.Promise;
import com.jingyuyao.tactical.model.world.Coordinate;
import java.util.List;

public class Moving implements Component, Poolable {

  private List<Coordinate> path = null;
  private Promise promise = null;
  private int currentIndex = 0;

  public List<Coordinate> getPath() {
    return path;
  }

  public void setPath(List<Coordinate> path) {
    this.path = path;
  }

  public Promise getPromise() {
    return promise;
  }

  public void setPromise(Promise promise) {
    this.promise = promise;
  }

  public int getCurrentIndex() {
    return currentIndex;
  }

  public void setCurrentIndex(int currentIndex) {
    this.currentIndex = currentIndex;
  }

  @Override
  public void reset() {
    path = null;
    promise = null;
    currentIndex = 0;
  }
}
