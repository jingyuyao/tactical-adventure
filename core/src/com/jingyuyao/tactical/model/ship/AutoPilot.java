package com.jingyuyao.tactical.model.ship;

import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.World;

public interface AutoPilot {

  PilotResponse getResponse(World world, Cell shipCell);
}
