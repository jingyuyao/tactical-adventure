package com.jingyuyao.tactical.model.character;

import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Movements;

public interface Enemy extends Character {

  Retaliation getRetaliation(Movements movements, Cell starting);
}
