package com.jingyuyao.tactical.model.item.event;

import com.google.common.util.concurrent.SettableFuture;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.event.FutureEvent;
import com.jingyuyao.tactical.model.item.Target;

public class Attack extends FutureEvent<Character> {

  private final Target target;

  public Attack(Character object, SettableFuture<Void> future, Target target) {
    super(object, future);
    this.target = target;
  }

  public Target getTarget() {
    return target;
  }
}
