package com.jingyuyao.tactical.model.item;

import com.jingyuyao.tactical.model.character.Character;

/**
 * A basic {@link Weapon} that does constant damage to all the {@link Target}.
 */
abstract class AbstractWeapon<T extends WeaponData> extends BaseItem<T> implements Weapon {

  AbstractWeapon(Character owner, T weaponStats) {
    super(owner, weaponStats);
  }

  @Override
  public void damages(Target target) {
    for (Character opponent : target.getTargetCharacters()) {
      opponent.damageBy(getData().getAttackPower());
    }
  }
}
