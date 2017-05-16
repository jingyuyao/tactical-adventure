package com.jingyuyao.tactical.model.terrain;

import com.jingyuyao.tactical.model.character.Ship;
import com.jingyuyao.tactical.model.i18n.Message;

public interface Terrain {

  Message getName();

  boolean canHold(Ship ship);

  int getMovementPenalty();
}
