package com.jingyuyao.tactical.model.item;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.map.Movements;
import com.jingyuyao.tactical.model.terrain.Terrain;
import javax.inject.Inject;

// TODO: test me
public class Grenade extends AbstractWeapon {

  private transient final Movements movements;
  private transient final TargetFactory targetFactory;
  private int distance;
  private int size;

  @Inject
  Grenade(Movements movements, TargetFactory targetFactory) {
    this.movements = movements;
    this.targetFactory = targetFactory;
  }

  @Override
  public ImmutableList<Target> createTargets(Coordinate from) {
    final Function<Terrain, Integer> weightFunction = new ConstWeight();
    return FluentIterable
        .from(movements.distanceFrom(from, distance, weightFunction).nodes())
        .transform(new Function<Coordinate, Target>() {
          @Override
          public Target apply(Coordinate input) {
            return targetFactory.create(
                ImmutableSet.of(input),
                movements.distanceFrom(input, size - 1, weightFunction).nodes());
          }
        })
        .toList();
  }

  private static class ConstWeight implements Function<Terrain, Integer> {

    @Override
    public Integer apply(Terrain input) {
      return 1;
    }
  }
}
