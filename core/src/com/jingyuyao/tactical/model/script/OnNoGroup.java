package com.jingyuyao.tactical.model.script;

import com.google.common.base.Objects;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.ship.ShipGroup;
import com.jingyuyao.tactical.model.state.Turn;
import com.jingyuyao.tactical.model.world.World;

/**
 * Satisfied when there are no ships of the given {@link ShipGroup} in the {@link World}.
 */
public class OnNoGroup extends Condition {

  private ShipGroup group;

  OnNoGroup() {
  }

  OnNoGroup(ShipGroup group) {
    this.group = group;
  }

  @Override
  public boolean onTurn(Turn turn, World world) {
    return onWorld(world);
  }

  @Override
  public boolean onShipDestroyed(Ship destroyed, World world) {
    return onWorld(world);
  }

  private boolean onWorld(World world) {
    for (Ship ship : world.getShipSnapshot().values()) {
      if (ship.inGroup(group)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    OnNoGroup that = (OnNoGroup) object;
    return group == that.group;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(group);
  }
}
