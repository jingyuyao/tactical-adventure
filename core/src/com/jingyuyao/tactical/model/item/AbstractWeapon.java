package com.jingyuyao.tactical.model.item;

import com.jingyuyao.tactical.model.character.Character;
import java.util.Locale;

/**
 * A basic {@link Weapon} that does constant damage to all the {@link Target}.
 */
// TODO: test me
abstract class AbstractWeapon extends BaseItem implements Weapon {

  private int attackPower;

  @Override
  public String getDescription() {
    return String.format(Locale.US, "Damages for %d", attackPower);
  }

  @Override
  public void damages(Target target) {
    for (Character opponent : target.getTargetCharacters()) {
      opponent.damageBy(attackPower);
    }
  }

  @Override
  public int getAttackPower() {
    return attackPower;
  }
}
