package com.jingyuyao.tactical.model.item;

import com.jingyuyao.tactical.model.resource.ModelBundle;
import com.jingyuyao.tactical.model.resource.ResourceKey;

public class Armor extends Item {

  private final int defense;

  Armor(String name, int usageLeft, int defense) {
    super(name, usageLeft);
    this.defense = defense;
  }

  @Override
  public ResourceKey getDescription() {
    return ModelBundle.ITEM_DESCRIPTION.get("armor", defense);
  }

  public int getDefense() {
    return defense;
  }
}
