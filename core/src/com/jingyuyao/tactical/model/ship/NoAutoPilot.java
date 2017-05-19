package com.jingyuyao.tactical.model.ship;

import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.World;

public class NoAutoPilot implements AutoPilot {

  @Override
  public PilotResponse getResponse(World world, Cell shipCell) {
    return new PilotResponse(null, null);
  }
}
