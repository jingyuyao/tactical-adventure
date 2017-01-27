package com.jingyuyao.tactical.model.item;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.jingyuyao.tactical.model.character.Character;

/**
 * A basic {@link Weapon} that does constant damage to all the {@link Target}.
 */
abstract class AbstractWeapon<T extends WeaponData> extends BaseItem<T> implements Weapon {

  AbstractWeapon(Character owner, T weaponStats) {
    super(owner, weaponStats);
  }

  @Override
  public ListenableFuture<Void> attack(final Target target) {
    ListenableFuture<Void> attackFuture = getOwner().attacks(target);
    Futures.addCallback(attackFuture, new FutureCallback<Void>() {
      @Override
      public void onSuccess(Void result) {
        for (Character opponent : target.getTargetCharacters()) {
          opponent.damageBy(getData().getAttackPower());
        }
        useOnce();
      }

      @Override
      public void onFailure(Throwable t) {

      }
    });
    return attackFuture;
  }
}
