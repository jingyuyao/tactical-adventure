package com.jingyuyao.tactical.model.script;

import com.jingyuyao.tactical.model.person.Person;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.state.Turn;
import com.jingyuyao.tactical.model.world.World;
import java.util.Set;

/**
 * Met when any of the {@link Person} in {@code names} are no longer in {@link World}.
 */
public class AnyDied extends BaseCondition {

  private Set<String> names;

  AnyDied() {
  }

  AnyDied(Set<String> names) {
    this.names = names;
  }

  @Override
  public boolean isMet(Turn turn, World world) {
    int met = 0;
    for (Ship ship : world.getShipSnapshot().values()) {
      for (Person crew : ship.getCrew()) {
        if (names.contains(crew.getName().getId())) {
          met++;
        }
      }
    }
    return names.size() != met;
  }
}
