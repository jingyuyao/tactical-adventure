package com.jingyuyao.tactical.model.event;

import com.google.common.util.concurrent.SettableFuture;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.item.Weapon;

public class Attack extends FutureEvent<Target> {

  private final Weapon weapon;

  public Attack(Target target, Weapon weapon, SettableFuture<Void> future) {
    super(target, future);
    this.weapon = weapon;
  }

  public Weapon getWeapon() {
    return weapon;
  }
}
