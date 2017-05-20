package com.jingyuyao.tactical.model.script;

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

  Died(String name) {
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
}
