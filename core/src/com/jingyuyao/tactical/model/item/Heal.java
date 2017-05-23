package com.jingyuyao.tactical.model.item;

import com.jingyuyao.tactical.model.resource.ModelBundle;
import com.jingyuyao.tactical.model.resource.ResourceKey;
import com.jingyuyao.tactical.model.ship.Ship;

public class Heal extends Consumable {

  private final int amount;

  Heal(String name, int usageLeft, int amount) {
    super(name, usageLeft);
    this.amount = amount;
  }

  @Override
  public ResourceKey getDescription() {
    return ModelBundle.ITEM_DESCRIPTION.get("heal", amount);
  }

  @Override
  public void apply(Ship ship) {
    ship.healBy(amount);
  }
}
