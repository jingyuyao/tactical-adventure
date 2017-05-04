package com.jingyuyao.tactical.model.item;

import com.jingyuyao.tactical.model.character.Character;
import java.util.Locale;

public class Heal extends AbstractItem implements Consumable {

  private int amount;

  Heal() {
  }

  Heal(String name, int usageLeft, int amount) {
    super(name, usageLeft);
    this.amount = amount;
  }

  @Override
  public String getDescription() {
    return String.format(Locale.US, "Heals for %d", amount);
  }

  @Override
  public void apply(Character character) {
    character.healBy(amount);
  }
}
