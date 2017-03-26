package com.jingyuyao.tactical.model.character;

import com.google.common.util.concurrent.ListenableFuture;
import com.jingyuyao.tactical.model.world.Cell;

public interface Enemy extends Character {

  ListenableFuture<Void> retaliate(Cell startingCell);
}
