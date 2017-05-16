package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.ship.Ship;

public class RemoveShip extends ObjectEvent<Ship> {

  public RemoveShip(Ship object) {
    super(object);
  }
}
