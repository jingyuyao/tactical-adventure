package com.jingyuyao.tactical.model.world;

public enum Direction {
  UP(0, 1),
  DOWN(0, -1),
  LEFT(-1, 0),
  RIGHT(1, 0);

  private final int xOffset;
  private final int yOffset;

  Direction(int xOffset, int yOffset) {
    this.xOffset = xOffset;
    this.yOffset = yOffset;
  }

  /**
   * Return the {@link Direction} to {@code to} starting at {@code from}.
   */
  public static Direction fromTo(Coordinate from, Coordinate to) {
    int xOffset = (int) Math.signum(to.getX() - from.getX());
    int yOffset = (int) Math.signum(to.getY() - from.getY());

    for (Direction direction : Direction.values()) {
      if (direction.getXOffset() == xOffset && direction.getYOffset() == yOffset) {
        return direction;
      }
    }
    throw new IllegalArgumentException(
        "To: " + to + " from: " + from + " is not in a valid direction");
  }

  public int getXOffset() {
    return xOffset;
  }

  public int getYOffset() {
    return yOffset;
  }
}
