package com.jingyuyao.tactical.model.item;

import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.map.Characters;
import java.util.Locale;

/**
 * A basic {@link Weapon} that does constant damage to all the {@link Target}.
 */
// TODO: test me
abstract class AbstractWeapon extends BaseItem implements Weapon {

  private final transient Characters characters;
  private int attackPower;

  AbstractWeapon(Characters characters) {
    this.characters = characters;
  }

  @Override
  public String getDescription() {
    return String.format(Locale.US, "Damages for %d", attackPower);
  }

  @Override
  public void damages(Target target) {
    // Convert to an immutable list since characters can die and we don't want an iteration error
    for (Character opponent : target.getTargetCharacters().toList()) {
      opponent.damageBy(attackPower);
      if (opponent.getHp() == 0) {
        characters.remove(opponent);
      }
    }
  }

  @Override
  public int getAttackPower() {
    return attackPower;
  }
}
