package com.jingyuyao.tactical.model.item;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.eventbus.EventBus;
import com.google.common.graph.Graph;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.common.Algorithms;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.map.Terrains;
import javax.inject.Inject;

public class Grenade extends AbstractWeapon<GrenadeStats> {

  private final Algorithms algorithms;
  private final Terrains terrains;
  private final TargetFactory targetFactory;

  @Inject
  Grenade(
      EventBus eventBus,
      @Assisted GrenadeStats grenadeStats,
      Algorithms algorithms,
      Terrains terrains,
      TargetFactory targetFactory) {
    super(eventBus, grenadeStats);
    this.algorithms = algorithms;
    this.terrains = terrains;
    this.targetFactory = targetFactory;
  }

  @Override
  public ImmutableList<Target> createTargets(Coordinate from) {
    Graph<Coordinate> selectCoordinates =
        algorithms.minPathSearch(
            terrains.getWidth(),
            terrains.getHeight(),
            new ConstWeight(),
            from,
            getItemStats().getDistance());
    ImmutableList.Builder<Target> builder = ImmutableList.builder();
    for (Coordinate select : selectCoordinates.nodes()) {
      Graph<Coordinate> targetCoordinates =
          algorithms.minPathSearch(
              terrains.getWidth(),
              terrains.getHeight(),
              new ConstWeight(),
              select,
              getItemStats().getSize()
          );
      builder.add(targetFactory.create(ImmutableSet.of(select), targetCoordinates.nodes()));
    }
    return builder.build();
  }

  private static class ConstWeight implements Function<Coordinate, Integer> {

    @Override
    public Integer apply(Coordinate input) {
      return 1;
    }
  }
}
