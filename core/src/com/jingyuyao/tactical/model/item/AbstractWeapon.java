package com.jingyuyao.tactical.model.item;

import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.world.Cell;
import java.util.Locale;

/**
 * A basic {@link Weapon} that does constant damage to all the {@link Target}.
 */
// TODO: test me
abstract class AbstractWeapon extends BaseItem implements Weapon {

  private int attackPower;

  @Override
  public String getDescription() {
    return String.format(Locale.US, "Damage all targeted with attack power of %d", attackPower);
  }

  /**
   * Default implementation damages every character in the target area equally. All defense is
   * respected.
   */
  @Override
  public void damages(Target target) {
    for (Cell cell : target.getTargetCells()) {
      for (Character character : cell.character().asSet()) {
        int damage = Math.max(attackPower - character.getDefense(), 0);
        character.damageBy(damage);
      }
    }
  }
}
