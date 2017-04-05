package com.jingyuyao.tactical.view.world2.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.jingyuyao.tactical.model.event.MyFuture;
import com.jingyuyao.tactical.model.world.Coordinate;
import java.util.List;

public class Moving implements Component, Poolable {

  private List<Coordinate> path = null;
  private MyFuture future = null;
  private int currentIndex = 0;

  public List<Coordinate> getPath() {
    return path;
  }

  public void setPath(List<Coordinate> path) {
    this.path = path;
  }

  public MyFuture getFuture() {
    return future;
  }

  public void setFuture(MyFuture future) {
    this.future = future;
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
    future = null;
    currentIndex = 0;
  }
}
