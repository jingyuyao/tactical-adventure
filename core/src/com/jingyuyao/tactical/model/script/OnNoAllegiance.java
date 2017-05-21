package com.jingyuyao.tactical.model.script;

import com.google.common.base.Objects;
import com.jingyuyao.tactical.model.person.Person;
import com.jingyuyao.tactical.model.ship.Allegiance;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.state.Turn;
import com.jingyuyao.tactical.model.world.World;

/**
 * Satisfied when there are no ships of the given {@link Allegiance} in the {@link World}.
 */
public class OnNoAllegiance extends Condition {

  private Allegiance allegiance;

  OnNoAllegiance() {
  }

  OnNoAllegiance(Allegiance allegiance) {
    this.allegiance = allegiance;
  }

  @Override
  public boolean onTurn(Turn turn, World world) {
    return onWorld(world);
  }

  @Override
  public boolean onDeath(Person person, World world) {
    return onWorld(world);
  }

  private boolean onWorld(World world) {
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
    OnNoAllegiance that = (OnNoAllegiance) object;
    return allegiance == that.allegiance;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(allegiance);
  }
}
