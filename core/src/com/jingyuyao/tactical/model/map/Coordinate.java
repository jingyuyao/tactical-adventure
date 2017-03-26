package com.jingyuyao.tactical.model.map;

import com.google.common.base.Objects;

/**
 * An object with a (x,y) coordinate. This class is the foundation for many of other classes and
 * algorithms so be careful when making changes.
 */
public final class Coordinate {

  // Do not expose setters!
  private int x;
  private int y;

  // No args constructor for serialization
  Coordinate() {
  }

  public Coordinate(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public Coordinate offsetBy(Direction direction) {
    return new Coordinate(x + direction.getXOffset(), y + direction.getYOffset());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Coordinate that = (Coordinate) o;
    return getX() == that.getX() && getY() == that.getY();
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getX(), getY());
  }
}
