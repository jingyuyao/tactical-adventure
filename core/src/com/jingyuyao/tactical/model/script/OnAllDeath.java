package com.jingyuyao.tactical.model.script;

import com.google.common.base.Objects;
import com.jingyuyao.tactical.model.person.Person;
import com.jingyuyao.tactical.model.world.World;
import java.util.HashSet;
import java.util.Set;

/**
 * Satisfied when this condition witnesses {@link Person} in {@code names} dies. <p>Limitations:
 * Will not capture a death when it occurs while this condition is not loaded in the active script.
 * The state of this condition needs to be persisted to save the captured deaths.
 */
public class OnAllDeath extends Condition {

  private Set<String> names;
  private Set<String> seen = new HashSet<>();

  OnAllDeath() {
  }

  OnAllDeath(Set<String> names) {
    this.names = names;
  }

  @Override
  boolean onDeath(Person person, World world) {
    String name = person.getName().getId();
    if (names.contains(name)) {
      seen.add(name);
    }
    return names.equals(seen);
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    OnAllDeath that = (OnAllDeath) object;
    return Objects.equal(names, that.names);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(names);
  }
}
