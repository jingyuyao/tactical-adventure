package com.jingyuyao.tactical.model.item;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
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
    return FluentIterable
        .from(world.getUnimpededMovement(from, distance).getCells())
        .transform(new Function<Cell, Target>() {
          @Override
          public Target apply(Cell input) {
            Set<Cell> targets =
                ImmutableSet.copyOf(world.getUnimpededMovement(input, size - 1).getCells());
            return new Target(input, ImmutableSet.of(input), targets);
          }
        })
        .toList();
  }
}
