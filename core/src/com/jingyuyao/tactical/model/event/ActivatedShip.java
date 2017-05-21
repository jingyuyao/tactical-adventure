package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.ship.Ship;

public class ActivatedShip extends ObjectEvent<Ship> {

  public ActivatedShip(Ship object) {
    super(object);
  }
}
