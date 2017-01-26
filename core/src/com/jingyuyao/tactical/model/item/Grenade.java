package com.jingyuyao.tactical.model.item;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.graph.Graph;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.map.Terrain;
import com.jingyuyao.tactical.model.map.TerrainGraphs;
import javax.inject.Inject;

// TODO: test me
public class Grenade extends AbstractWeapon<GrenadeStats> {

  private final TerrainGraphs terrainGraphs;
  private final TargetFactory targetFactory;

  @Inject
  Grenade(
      @Assisted Character owner,
      @Assisted GrenadeStats grenadeStats,
      TerrainGraphs terrainGraphs,
      TargetFactory targetFactory) {
    super(owner, grenadeStats);
    this.terrainGraphs = terrainGraphs;
    this.targetFactory = targetFactory;
  }

  @Override
  public ImmutableList<Target> createTargets(Coordinate from) {
    Graph<Coordinate> selectCoordinates =
        terrainGraphs.distanceFromGraph(new ConstWeight(), from, getItemStats().getDistance());

    ImmutableList.Builder<Target> builder = ImmutableList.builder();
    for (Coordinate select : selectCoordinates.nodes()) {
      Graph<Coordinate> targetCoordinates =
          terrainGraphs.distanceFromGraph(new ConstWeight(), select, getItemStats().getSize());
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
