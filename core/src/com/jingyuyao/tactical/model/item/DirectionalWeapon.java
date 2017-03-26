package com.jingyuyao.tactical.model.item;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.jingyuyao.tactical.model.World;
import com.jingyuyao.tactical.model.map.Cell;
import com.jingyuyao.tactical.model.map.Direction;
import javax.inject.Inject;

/**
 * A weapon that can be targeted in all directions in {@link Direction#values()}.
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
    for (Direction direction : Direction.values()) {
      Optional<Target> targetOptional = createTarget(from, direction);
      if (targetOptional.isPresent()) {
        builder.add(targetOptional.get());
      }
    }
    return builder.build();
  }

  private Optional<Target> createTarget(Cell from, Direction direction) {
    ImmutableSet.Builder<Cell> targetBuilder = ImmutableSet.builder();
    Cell current = from;
    int leftOverDistance = distance;

    while (leftOverDistance > 0) {
      Optional<Cell> neighbor = world.getNeighbor(current, direction);
      if (neighbor.isPresent()) {
        current = neighbor.get();
        targetBuilder.add(current);
        leftOverDistance--;
      } else {
        break;
      }
    }

    ImmutableSet<Cell> targets = targetBuilder.build();
    if (targets.isEmpty()) {
      return Optional.absent();
    }

    ImmutableSet<Cell> select = ImmutableSet.of(targets.iterator().next());
    return Optional.of(new Target(select, targets));
  }
}
