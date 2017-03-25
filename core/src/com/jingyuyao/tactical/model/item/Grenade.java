package com.jingyuyao.tactical.model.item;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.jingyuyao.tactical.model.map.Cell;
import com.jingyuyao.tactical.model.map.Movements;
import javax.inject.Inject;

// TODO: test me
public class Grenade extends AbstractWeapon {

  private transient final Movements movements;
  private int distance;
  private int size;

  @Inject
  Grenade(Movements movements) {
    this.movements = movements;
  }

  @Override
  public ImmutableList<Target> createTargets(Cell from) {
    final Function<Cell, Integer> weightFunction = new ConstWeight();
    return FluentIterable
        .from(movements.distanceFrom(from, distance, weightFunction).nodes())
        .transform(new Function<Cell, Target>() {
          @Override
          public Target apply(Cell input) {
            return new Target(
                ImmutableSet.of(input),
                movements.distanceFrom(input, size - 1, weightFunction).nodes());
          }
        })
        .toList();
  }

  private static class ConstWeight implements Function<Cell, Integer> {

    @Override
    public Integer apply(Cell input) {
      return 1;
    }
  }
}
