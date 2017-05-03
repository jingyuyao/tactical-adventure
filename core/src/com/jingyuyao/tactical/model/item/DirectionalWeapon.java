package com.jingyuyao.tactical.model.item;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Direction;
import com.jingyuyao.tactical.model.world.Movements;

/**
 * A weapon that can be targeted in all directions in {@link Direction#values()}.
 */
// TODO: test me
public class DirectionalWeapon extends BaseWeapon {

  private int distance;

  @Override
  public ImmutableList<Target> createTargets(Movements movements, Cell from) {
    ImmutableList.Builder<Target> builder = ImmutableList.builder();
    for (Direction direction : Direction.values()) {
      Optional<Target> targetOptional = createTarget(movements, from, direction);
      if (targetOptional.isPresent()) {
        builder.add(targetOptional.get());
      }
    }
    return builder.build();
  }

  private Optional<Target> createTarget(Movements movements, Cell from, Direction direction) {
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
