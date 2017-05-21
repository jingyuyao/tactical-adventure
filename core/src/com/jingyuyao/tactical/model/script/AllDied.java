package com.jingyuyao.tactical.model.script;

import com.google.common.base.Objects;
import com.jingyuyao.tactical.model.person.Person;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.state.Turn;
import com.jingyuyao.tactical.model.world.World;
import java.util.Set;

/**
 * Met when all of the {@link Person} in {@code names} are no longer in {@link World}.
 */
public class AllDied extends BaseCondition {

  private Set<String> names;

  AllDied() {
  }

  AllDied(Set<String> names) {
    this.names = names;
  }

  @Override
  public boolean isMet(Turn turn, World world) {
    boolean met = true;
    for (Ship ship : world.getShipSnapshot().values()) {
      for (Person crew : ship.getCrew()) {
        if (names.contains(crew.getName().getId())) {
          met = false;
        }
      }
    }
    return met;
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
    AllDied allDied = (AllDied) object;
    return Objects.equal(names, allDied.names);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(super.hashCode(), names);
  }
}
