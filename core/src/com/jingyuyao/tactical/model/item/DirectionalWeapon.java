package com.jingyuyao.tactical.model.item;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Direction;
import com.jingyuyao.tactical.model.world.Movements;
import javax.inject.Inject;

/**
 * A weapon that can be targeted in all directions in {@link Direction#values()}.
 */
// TODO: test me
public class DirectionalWeapon extends BaseWeapon {

  private transient final Movements movements;
  private int distance;

  @Inject
  DirectionalWeapon(Movements movements) {
    this.movements = movements;
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
      Optional<Cell> neighbor = movements.getNeighbor(current, direction);
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

    Cell origin = targets.iterator().next();
    ImmutableSet<Cell> selects = ImmutableSet.of(origin);
    return Optional.of(new Target(origin, direction, selects, targets));
  }
}
