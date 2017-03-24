package com.jingyuyao.tactical.model.map.event;

import com.google.common.util.concurrent.SettableFuture;
import com.jingyuyao.tactical.model.event.FutureEvent;
import com.jingyuyao.tactical.model.map.Path;

public class MoveCharacter extends FutureEvent<Path> {

  public MoveCharacter(Path object, SettableFuture<Void> future) {
    super(object, future);
  }
}
