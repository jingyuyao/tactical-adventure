package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.ship.Ship;

public class ActivatedEnemy extends ObjectEvent<Ship> {

  public ActivatedEnemy(Ship object) {
    super(object);
  }
}
