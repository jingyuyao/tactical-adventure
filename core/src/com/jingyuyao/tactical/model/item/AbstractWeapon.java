package com.jingyuyao.tactical.model.item;

import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.item.event.Attack;

/**
 * A basic {@link Weapon} that does constant damage to all the {@link Target}.
 */
abstract class AbstractWeapon<T extends WeaponStats> extends BaseItem<T> implements Weapon {

  AbstractWeapon(EventBus eventBus, T weaponStats) {
    super(eventBus, weaponStats);
  }

  @Override
  public ListenableFuture<Void> attack(Character attacker, final Target target) {
    for (Character opponent : target.getTargetCharacters()) {
      opponent.damageBy(getItemStats().getAttackPower());
    }
    useOnce();
    SettableFuture<Void> future = SettableFuture.create();
    getEventBus().post(new Attack(attacker, future, target));
    return future;
  }
}
