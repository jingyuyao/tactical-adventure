package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.item.Weapon;

public class Attack extends ObjectEvent<Target> {

  private final Weapon weapon;
  private final MyFuture future;

  public Attack(Target target, Weapon weapon, MyFuture future) {
    super(target);
    this.weapon = weapon;
    this.future = future;
  }

  public Weapon getWeapon() {
    return weapon;
  }

  public MyFuture getFuture() {
    return future;
  }
}
