package com.jingyuyao.tactical.model.character;

import com.jingyuyao.tactical.model.event.MyFuture;
import com.jingyuyao.tactical.model.world.Cell;

public interface Enemy extends Character {

  MyFuture retaliate(Cell startingCell);
}
