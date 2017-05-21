package com.jingyuyao.tactical.model.script;

import com.google.common.base.Objects;
import com.jingyuyao.tactical.model.person.Person;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.state.Turn;
import com.jingyuyao.tactical.model.world.World;

/**
 * Met when a {@link Person} with {@code name} is no longer in the {@link World}.
 */
public class Died extends BaseCondition {

  private String name;

  Died() {
  }

  public Died(String name) {
    this.name = name;
  }

  @Override
  public boolean isMet(Turn turn, World world) {
    for (Ship ship : world.getShipSnapshot().values()) {
      for (Person crew : ship.getCrew()) {
        if (name.equals(crew.getName().getId())) {
          return false;
        }
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
    Died died = (Died) object;
    return Objects.equal(name, died.name);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(super.hashCode(), name);
  }
}
