package com.jingyuyao.tactical.model.item;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.graph.Graph;
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
    Graph<Coordinate> selectCoordinates =
        movements.distanceFrom(from, distance, new ConstWeight());

    ImmutableList.Builder<Target> builder = ImmutableList.builder();
    for (Coordinate select : selectCoordinates.nodes()) {
      Graph<Coordinate> targetCoordinates =
          movements.distanceFrom(select, size, new ConstWeight());
      builder.add(targetFactory.create(ImmutableSet.of(select), targetCoordinates.nodes()));
    }
    return builder.build();
  }

  private static class ConstWeight implements Function<Terrain, Integer> {

    @Override
    public Integer apply(Terrain input) {
      return 1;
    }
  }
}
