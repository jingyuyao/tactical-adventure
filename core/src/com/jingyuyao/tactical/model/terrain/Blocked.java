package com.jingyuyao.tactical.model.terrain;

import com.jingyuyao.tactical.model.i18n.Message;
import com.jingyuyao.tactical.model.i18n.ModelBundle;
import com.jingyuyao.tactical.model.ship.Ship;

public class Blocked implements Terrain {

  @Override
  public Message getName() {
    return ModelBundle.TERRAIN_NAME.get("blocked");
  }

  @Override
  public boolean canHold(Ship ship) {
    return false;
  }

  @Override
  public int getMovementPenalty() {
    return 0;
  }
}
