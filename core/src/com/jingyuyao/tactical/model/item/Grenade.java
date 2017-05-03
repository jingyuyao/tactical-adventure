package com.jingyuyao.tactical.model.item;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Movements;
import java.util.Set;
import javax.inject.Inject;

// TODO: test me
public class Grenade extends BaseWeapon {

  private transient final Movements movements;
  private int distance;
  private int size;

  @Inject
  Grenade(Movements movements) {
    this.movements = movements;
  }

  @Override
  public ImmutableList<Target> createTargets(Cell from) {
    return FluentIterable
        .from(movements.distanceFrom(from, distance).getCells())
        .transform(new Function<Cell, Target>() {
          @Override
          public Target apply(Cell input) {
            Set<Cell> targets =
                ImmutableSet.copyOf(movements.distanceFrom(input, size - 1).getCells());
            return new Target(input, ImmutableSet.of(input), targets);
          }
        })
        .toList();
  }
}
