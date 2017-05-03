package com.jingyuyao.tactical.model.character;

import com.jingyuyao.tactical.model.battle.Battle;
import com.jingyuyao.tactical.model.event.MyFuture;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Movements;

public interface Enemy extends Character {

  MyFuture retaliate(Movements movements, Battle battle, Cell startingCell);
}
