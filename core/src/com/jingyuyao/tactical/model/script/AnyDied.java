package com.jingyuyao.tactical.model.script;

import com.jingyuyao.tactical.model.person.Person;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.state.Turn;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.World;
import java.util.Set;

/**
 * A {@link Condition} that is met when any of the {@link Person} in {@code names} is dead.
 */
public class AnyDied implements Condition {

  private Set<String> names;

  AnyDied() {
  }

  AnyDied(Set<String> names) {
    this.names = names;
  }

  @Override
  public boolean isMet(Turn turn, World world) {
    int met = 0;
    for (Cell cell : world.getShipSnapshot()) {
      for (Ship ship : cell.ship().asSet()) {
        for (Person crew : ship.getCrew()) {
          if (names.contains(crew.getName().getId())) {
            met++;
          }
        }
      }
    }
    return names.size() != met;
  }
}
