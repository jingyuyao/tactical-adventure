package com.jingyuyao.tactical.model.world;

import com.google.common.base.Objects;
import java.util.Locale;

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

  Coordinate offsetBy(Direction direction) {
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

  @Override
  public String toString() {
    return String.format(Locale.US, "(%d,%d)", x, y);
  }
}
