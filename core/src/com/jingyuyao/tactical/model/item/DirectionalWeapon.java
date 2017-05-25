package com.jingyuyao.tactical.model.item;

import com.google.common.base.Optional;
import com.jingyuyao.tactical.model.battle.Target;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Direction;
import com.jingyuyao.tactical.model.world.World;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A weapon that can be targeted in all directions in {@link Direction#values()}.
 */
// TODO: test me
public class DirectionalWeapon extends Weapon {

  private int distance;

  DirectionalWeapon() {
  }

  @Override
  public List<Target> createTargets(World world, Cell from) {
    List<Target> targets = new ArrayList<>();
    for (Direction direction : Direction.values()) {
      targets.addAll(createTarget(world, from, direction).asSet());
    }
    return targets;
  }

  private Optional<Target> createTarget(World world, Cell from, Direction direction) {
    Cell current = from;
    int leftOverDistance = distance;
    Set<Cell> targetCells = new HashSet<>();
    Cell origin = null;

    while (leftOverDistance > 0) {
      Optional<Cell> neighbor = world.neighbor(current, direction);
      if (neighbor.isPresent()) {
        current = neighbor.get();
        targetCells.add(current);
        if (origin == null) {
          origin = current;
        }
        leftOverDistance--;
      } else {
        break;
      }
    }

    if (targetCells.isEmpty() || origin == null) {
      return Optional.absent();
    }

    Set<Cell> selects = Collections.singleton(origin);
    return Optional.of(new Target(origin, direction, selects, targetCells));
  }
}
