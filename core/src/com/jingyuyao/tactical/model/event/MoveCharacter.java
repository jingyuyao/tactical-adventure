package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.character.Ship;
import com.jingyuyao.tactical.model.world.Path;

public class MoveCharacter {

  private final Ship ship;
  private final Path path;
  private final Promise promise;

  public MoveCharacter(Ship ship, Path path, Promise promise) {
    this.ship = ship;
    this.path = path;
    this.promise = promise;
  }

  public Ship getShip() {
    return ship;
  }

  public Path getPath() {
    return path;
  }

  public Promise getPromise() {
    return promise;
  }
}
