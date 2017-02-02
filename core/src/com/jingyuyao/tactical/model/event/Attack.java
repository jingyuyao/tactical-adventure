package com.jingyuyao.tactical.model.event;

import com.google.common.util.concurrent.SettableFuture;
import com.jingyuyao.tactical.model.item.Target;

public class Attack extends FutureEvent<Target> {

  public Attack(Target target, SettableFuture<Void> future) {
    super(target, future);
  }
}
