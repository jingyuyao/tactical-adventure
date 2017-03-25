package com.jingyuyao.tactical.model.item;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.jingyuyao.tactical.model.World;
import com.jingyuyao.tactical.model.map.Cell;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.map.Directions;
import javax.inject.Inject;

/**
 * A weapon that can be targeted in all directions in {@link Directions#ALL}.
 */
// TODO: test me
public class DirectionalWeapon extends AbstractWeapon {

  private transient final World world;
  private int distance;

  @Inject
  DirectionalWeapon(World world) {
    this.world = world;
  }

  @Override
  public ImmutableList<Target> createTargets(Cell from) {
    ImmutableList.Builder<Target> builder = ImmutableList.builder();
    for (Coordinate direction : Directions.ALL) {
      Optional<Target> targetOptional = createTarget(from, direction);
      if (targetOptional.isPresent()) {
        builder.add(targetOptional.get());
      }
    }
    return builder.build();
  }

  private Optional<Target> createTarget(Cell from, Coordinate direction) {
    Coordinate current = from.getCoordinate().offsetBy(direction);
    if (!world.hasCoordinate(current)) {
      return Optional.absent();
    }
    ImmutableSet<Cell> selectCells = ImmutableSet.of(world.getCell(current));
    int leftOverDistance = distance;

    ImmutableSet.Builder<Cell> targetBuilder = ImmutableSet.builder();
    while (leftOverDistance > 0 && world.hasCoordinate(current)) {
      targetBuilder.add(world.getCell(current));
      current = current.offsetBy(direction);
      leftOverDistance--;
    }

    return Optional.of(new Target(selectCells, targetBuilder.build()));
  }
}
