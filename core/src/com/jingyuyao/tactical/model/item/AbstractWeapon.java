package com.jingyuyao.tactical.model.item;

import com.jingyuyao.tactical.model.character.Character;
import java.util.Locale;

/**
 * A basic {@link Weapon} that does constant damage to all the {@link Target}.
 */
abstract class AbstractWeapon<T extends WeaponData> extends BaseItem<T> implements Weapon {

  AbstractWeapon(T weaponStats) {
    super(weaponStats);
  }

  @Override
  public void damages(Target target) {
    for (Character opponent : target.getTargetCharacters()) {
      opponent.damageBy(getData().getAttackPower());
    }
  }

  @Override
  public String toString() {
    return String.format(
        Locale.US, "%s\nAtk(%d) Usg(%d)", getName(), getData().getAttackPower(), getUsageLeft());
  }
}
