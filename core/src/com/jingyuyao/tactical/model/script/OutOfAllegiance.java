package com.jingyuyao.tactical.model.script;

import com.google.common.base.Objects;
import com.jingyuyao.tactical.model.Allegiance;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.state.Turn;
import com.jingyuyao.tactical.model.world.World;

/**
 * Met when there are no ships of the given {@link Allegiance} in the world.
 */
public class OutOfAllegiance extends BaseCondition {

  private Allegiance allegiance;

  OutOfAllegiance() {
  }

  OutOfAllegiance(Allegiance allegiance) {
    this.allegiance = allegiance;
  }

  @Override
  public boolean isMet(Turn turn, World world) {
    for (Ship ship : world.getShipSnapshot().values()) {
      if (ship.getAllegiance().equals(allegiance)) {
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
    if (!super.equals(object)) {
      return false;
    }
    OutOfAllegiance that = (OutOfAllegiance) object;
    return allegiance == that.allegiance;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(super.hashCode(), allegiance);
  }
}
