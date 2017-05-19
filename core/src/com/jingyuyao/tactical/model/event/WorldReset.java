package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.world.World;

public class WorldReset {

  private final World world;

  public WorldReset(World world) {
    this.world = world;
  }

  public World getWorld() {
    return world;
  }
}
