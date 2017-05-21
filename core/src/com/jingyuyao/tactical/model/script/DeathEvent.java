package com.jingyuyao.tactical.model.script;

import com.jingyuyao.tactical.model.person.Person;
import com.jingyuyao.tactical.model.world.World;

public class DeathEvent implements ScriptEvent {

  private final Person death;
  private final World world;

  public DeathEvent(Person death, World world) {
    this.death = death;
    this.world = world;
  }

  @Override
  public boolean satisfiedBy(Condition condition) {
    return condition.onDeath(death, world);
  }

  public Person getDeath() {
    return death;
  }

  public World getWorld() {
    return world;
  }
}
