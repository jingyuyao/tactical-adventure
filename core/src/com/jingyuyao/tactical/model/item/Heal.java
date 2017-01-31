package com.jingyuyao.tactical.model.item;

import com.jingyuyao.tactical.model.character.Character;
import java.util.Locale;

public class Heal extends BaseItem implements Consumable {

  private int amount;

  private Heal() {
  }

  Heal(String name, int usageLeft, int amount) {
    super(name, usageLeft);
    this.amount = amount;
  }

  @Override
  public void apply(Character character) {
    character.healBy(amount);
  }

  @Override
  public String toString() {
    return String.format(
        Locale.US, "%s\nHeal(%d) Usg(%d)", getName(), amount, getUsageLeft());
  }
}
