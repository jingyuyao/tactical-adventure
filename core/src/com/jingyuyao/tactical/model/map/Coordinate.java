package com.jingyuyao.tactical.model.map;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * An object with a (x,y) coordinate. This class is the foundation for many of other classes and
 * algorithms so be careful when making changes.
 */
public final class Coordinate {

  private final int x;
  private final int y;

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

  public Coordinate offsetBy(Coordinate coordinate) {
    return new Coordinate(x + coordinate.x, y + coordinate.y);
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

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("x", x).add("y", y).toString();
  }
}