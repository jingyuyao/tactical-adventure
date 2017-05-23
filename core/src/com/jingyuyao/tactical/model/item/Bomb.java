package com.jingyuyao.tactical.model.item;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.jingyuyao.tactical.model.battle.Target;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.World;
import java.util.Set;

// TODO: test me
public class Bomb extends Weapon {

  private final int distance;
  private final int size;

  Bomb(String name, int usageLeft, int attackPower,
      float lifeStealRate, float recoilRate, int distance, int size) {
    super(name, usageLeft, attackPower, lifeStealRate, recoilRate);
    this.distance = distance;
    this.size = size;
  }

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
