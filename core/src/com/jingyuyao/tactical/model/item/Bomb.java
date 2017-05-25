package com.jingyuyao.tactical.model.item;

import com.jingyuyao.tactical.model.battle.Target;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.World;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

// TODO: test me
public class Bomb extends Weapon {

  private int distance;
  private int size;

  Bomb() {
  }

  @Override
  public List<Target> createTargets(final World world, Cell from) {
    List<Target> targets = new ArrayList<>();
    for (Cell cell : world.getUnimpededMovement(from, distance).getCells()) {
      Set<Cell> targetCells = world.getUnimpededMovement(cell, size - 1).getCells();
      targets.add(new Target(cell, Collections.singleton(cell), targetCells));
    }
    return targets;
  }
}
