package com.jingyuyao.tactical.model.character;

import com.google.common.util.concurrent.ListenableFuture;

public interface Enemy extends Character {

  ListenableFuture<Void> retaliate();
}
