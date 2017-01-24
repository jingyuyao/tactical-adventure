package com.jingyuyao.tactical.model.item;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.character.Character;

/**
 * A basic {@link Weapon} that does constant damage to all the {@link Target}.
 */
abstract class AbstractWeapon<T extends WeaponStats> extends BaseItem<T> implements Weapon {

  AbstractWeapon(EventBus eventBus, T weaponStats) {
    super(eventBus, weaponStats);
  }

  @Override
  public void attack(Character attacker, Target target) {
    for (Character opponent : target.getTargetCharacters()) {
      opponent.damageBy(getItemStats().getAttackPower());
    }
    useOnce();
  }
}
