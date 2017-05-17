package com.jingyuyao.tactical.model.ship;

import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Movements;

public class NoAutoPilot implements AutoPilot {

  @Override
  public PilotResponse getResponse(Cell shipCell, Movements movements) {
    return new PilotResponse(null, null);
  }
}
