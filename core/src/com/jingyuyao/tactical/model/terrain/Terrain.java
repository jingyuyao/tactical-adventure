package com.jingyuyao.tactical.model.terrain;

import com.jingyuyao.tactical.model.resource.Message;
import com.jingyuyao.tactical.model.ship.Ship;

public interface Terrain {

  Message getName();

  boolean canHold(Ship ship);

  int getMovementPenalty();
}
