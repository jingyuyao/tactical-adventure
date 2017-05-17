package com.jingyuyao.tactical.model.ship;

import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Movements;

public interface AutoPilot {

  PilotResponse getResponse(Cell shipCell, Movements movements);
}
