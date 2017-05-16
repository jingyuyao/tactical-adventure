package com.jingyuyao.tactical.model.ship;

import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Movements;

public interface Enemy extends Ship {

  Retaliation getRetaliation(Movements movements, Cell starting);
}
