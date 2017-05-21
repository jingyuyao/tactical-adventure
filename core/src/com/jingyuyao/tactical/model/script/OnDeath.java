package com.jingyuyao.tactical.model.script;

import com.google.common.base.Objects;
import com.jingyuyao.tactical.model.person.Person;
import com.jingyuyao.tactical.model.world.World;

/**
 * Satisfied when a {@link Person} with {@code name} just died.
 */
public class OnDeath extends Condition {

  private String name;

  OnDeath() {
  }

  public OnDeath(String name) {
    this.name = name;
  }

  @Override
  public boolean onDeath(Person person, World world) {
    return name.equals(person.getName().getId());
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
