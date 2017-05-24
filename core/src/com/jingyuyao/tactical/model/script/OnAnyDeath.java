package com.jingyuyao.tactical.model.script;

import com.google.common.base.Objects;
import com.jingyuyao.tactical.model.person.Person;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.world.World;
import java.util.Set;

/**
 * Satisfied when any of the {@link Person} in {@code names} just died.
 */
public class OnAnyDeath extends Condition {

  private final Set<String> names;

  OnAnyDeath(Set<String> names) {
    this.names = names;
  }

  @Override
  public boolean onShipDestroyed(Ship ship, World world) {
    for (Person person : ship.getCrew()) {
      if (names.contains(person.getName().getId())) {
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
    OnAnyDeath that = (OnAnyDeath) object;
    return Objects.equal(names, that.names);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(names);
  }
}
