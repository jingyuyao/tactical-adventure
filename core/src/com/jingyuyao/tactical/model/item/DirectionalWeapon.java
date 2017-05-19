package com.jingyuyao.tactical.model.item;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.jingyuyao.tactical.model.battle.Target;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Direction;
import com.jingyuyao.tactical.model.world.World;

/**
 * A weapon that can be targeted in all directions in {@link Direction#values()}.
 */
// TODO: test me
public class DirectionalWeapon extends BaseWeapon {

  private int distance;

  @Override
  public ImmutableList<Target> createTargets(World world, Cell from) {
    ImmutableList.Builder<Target> builder = ImmutableList.builder();
    for (Direction direction : Direction.values()) {
      Optional<Target> targetOptional = createTarget(world, from, direction);
      if (targetOptional.isPresent()) {
        builder.add(targetOptional.get());
      }
    }
    return builder.build();
  }

  private Optional<Target> createTarget(World world, Cell from, Direction direction) {
    ImmutableSet.Builder<Cell> targetBuilder = ImmutableSet.builder();
    Cell current = from;
    int leftOverDistance = distance;

    while (leftOverDistance > 0) {
      Optional<Cell> neighbor = world.neighbor(current, direction);
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
