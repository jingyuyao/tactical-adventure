package com.jingyuyao.tactical.model.ship;

import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.World;
import java.io.Serializable;

public interface AutoPilot extends Serializable {

  PilotResponse getResponse(World world, Cell shipCell);
}
