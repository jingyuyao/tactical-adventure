package com.jingyuyao.tactical.model.retaliation;

import com.google.common.util.concurrent.ListenableFuture;
import com.jingyuyao.tactical.model.character.Enemy;

public interface Retaliation {

  ListenableFuture<Void> run(Enemy enemy);
}
