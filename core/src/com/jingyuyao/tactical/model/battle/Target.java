package com.jingyuyao.tactical.model.battle;

import com.google.common.base.Optional;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Direction;
import java.util.Set;

public class Target {

  private final Cell origin;
  private final Direction direction;
  private final Set<Cell> selectCells;
  private final Set<Cell> targetCells;

  public Target(Cell origin, Set<Cell> selectCells, Set<Cell> targetCells) {
    this(origin, null, selectCells, targetCells);
  }

  public Target(Cell origin, Direction direction, Set<Cell> selectCells, Set<Cell> targetCells) {
    this.origin = origin;
    this.direction = direction;
    this.selectCells = selectCells;
    this.targetCells = targetCells;
  }

  public Cell getOrigin() {
    return origin;
  }

  public Optional<Direction> direction() {
    return Optional.fromNullable(direction);
  }

  public boolean selectedBy(Cell cell) {
    return selectCells.contains(cell);
  }

  public boolean canTarget(Cell cell) {
    return targetCells.contains(cell);
  }

  public Set<Cell> getSelectCells() {
    return selectCells;
  }

  public Set<Cell> getTargetCells() {
    return targetCells;
  }
}
