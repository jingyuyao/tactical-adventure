package com.jingyuyao.tactical.model.script;

import com.google.common.base.Objects;
import com.jingyuyao.tactical.model.person.Person;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.world.World;

/**
 * Satisfied when a {@link Person} with {@code name} just died.
 */
public class OnDeath extends Condition {

  private final String name;

  public OnDeath(String name) {
    this.name = name;
  }

  @Override
  public boolean onShipDestroyed(Ship destroyed, World world) {
    for (Person person : destroyed.getCrew()) {
      if (name.equals(person.getName().getId())) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    OnDeath onDeath = (OnDeath) object;
    return Objects.equal(name, onDeath.name);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name);
  }
}
