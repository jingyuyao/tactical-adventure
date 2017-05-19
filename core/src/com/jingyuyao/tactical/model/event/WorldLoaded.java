package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.world.World;

public class WorldLoaded {

  private final World world;

  public WorldLoaded(World world) {
    this.world = world;
  }

  public World getWorld() {
    return world;
  }
}
