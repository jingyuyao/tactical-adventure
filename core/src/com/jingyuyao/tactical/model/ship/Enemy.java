package com.jingyuyao.tactical.model.ship;

import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Movements;

public interface Enemy extends Ship {

  AutoPilot getAutoPilot(Movements movements, Cell starting);
}
