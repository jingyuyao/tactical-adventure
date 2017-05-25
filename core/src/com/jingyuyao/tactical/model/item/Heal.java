package com.jingyuyao.tactical.model.item;

import com.jingyuyao.tactical.model.resource.ModelBundle;
import com.jingyuyao.tactical.model.resource.StringKey;
import com.jingyuyao.tactical.model.ship.Ship;

public class Heal extends Consumable {

  private int amount;

  Heal() {
  }

  @Override
  public StringKey getDescription() {
    return ModelBundle.ITEM_DESCRIPTION.get("heal", amount);
  }

  @Override
  public void apply(Ship ship) {
    ship.healBy(amount);
  }
}
