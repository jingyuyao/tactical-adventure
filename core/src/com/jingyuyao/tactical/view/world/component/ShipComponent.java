package com.jingyuyao.tactical.view.world.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.jingyuyao.tactical.model.ship.Ship;

public class ShipComponent implements Component, Poolable {

  private Ship ship;

  public Ship getShip() {
    return ship;
  }

  public void setShip(Ship ship) {
    this.ship = ship;
  }

  @Override
  public void reset() {
    ship = null;
  }
}
