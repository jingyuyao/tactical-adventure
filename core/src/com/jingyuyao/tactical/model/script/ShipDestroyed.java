package com.jingyuyao.tactical.model.script;

import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.world.World;

public class ShipDestroyed implements ScriptEvent {

  private final Ship destroyed;
  private final World world;

  public ShipDestroyed(Ship destroyed, World world) {
    this.destroyed = destroyed;
    this.world = world;
  }

  @Override
  public boolean satisfiedBy(Condition condition) {
    return condition.onShipDestroyed(destroyed, world);
  }

  public Ship getDestroyed() {
    return destroyed;
  }

  public World getWorld() {
    return world;
  }
}
