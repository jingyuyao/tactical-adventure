package com.jingyuyao.tactical.model.item;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.character.Character;

/**
 * A basic {@link Weapon} that does constant damage to all the {@link Target}.
 */
abstract class AbstractWeapon extends BaseItem<WeaponStats> implements Weapon {

  AbstractWeapon(EventBus eventBus, WeaponStats itemStats) {
    super(eventBus, itemStats);
  }

  @Override
  public void execute(Character attacker, Target target) {
    for (Character opponent : target.getTargetCharacters()) {
      opponent.damageBy(getItemStats().getAttackPower());
    }
    useOnce();
  }
}
