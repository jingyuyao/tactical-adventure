package com.jingyuyao.tactical.model.battle;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Direction;
import java.util.Set;

public class Target {

  private final Cell origin;
  private final Direction direction;
  private final ImmutableSet<Cell> selectCells;
  private final ImmutableSet<Cell> targetCells;

  public Target(Cell origin, Set<Cell> selectCells, Set<Cell> targetCells) {
    this(origin, null, selectCells, targetCells);
  }

  public Target(Cell origin, Direction direction, Set<Cell> selectCells, Set<Cell> targetCells) {
    this.origin = origin;
    this.direction = direction;
    this.selectCells = ImmutableSet.copyOf(selectCells);
    this.targetCells = ImmutableSet.copyOf(targetCells);
  }

  public Cell getOrigin() {
    return origin;
  }

  public Optional<Direction> getDirection() {
    return Optional.fromNullable(direction);
  }

  public boolean selectedBy(Cell cell) {
    return selectCells.contains(cell);
  }

  public boolean canTarget(Cell cell) {
    return targetCells.contains(cell);
  }

  public ImmutableSet<Cell> getSelectCells() {
    return selectCells;
  }

  public ImmutableSet<Cell> getTargetCells() {
    return targetCells;
  }
}
