package com.jingyuyao.tactical.model.item;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.jingyuyao.tactical.model.battle.Target;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.World;
import java.util.Set;

// TODO: test me
public class Bomb extends BaseWeapon {

  private int distance;
  private int size;

  @Override
  public ImmutableList<Target> createTargets(final World world, Cell from) {
    ImmutableList.Builder<Target> builder = new ImmutableList.Builder<>();
    for (Cell cell : world.getUnimpededMovement(from, distance).getCells()) {
      Set<Cell> targets =
          ImmutableSet.copyOf(world.getUnimpededMovement(cell, size - 1).getCells());
      builder.add(new Target(cell, ImmutableSet.of(cell), targets));
    }
    return builder.build();
  }
}
