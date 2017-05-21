package com.jingyuyao.tactical.model.script;

import com.google.common.base.Objects;
import com.jingyuyao.tactical.model.person.Person;
import com.jingyuyao.tactical.model.world.World;
import java.util.Set;

/**
 * Satisfied when any of the {@link Person} in {@code names} just died.
 */
public class OnAnyDeath extends Condition {

  private Set<String> names;

  OnAnyDeath() {
  }

  OnAnyDeath(Set<String> names) {
    this.names = names;
  }

  @Override
  public boolean onDeath(Person person, World world) {
    return names.contains(person.getName().getId());
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
