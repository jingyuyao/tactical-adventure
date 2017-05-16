package com.jingyuyao.tactical.model.item;

import com.jingyuyao.tactical.model.resource.ModelBundle;
import com.jingyuyao.tactical.model.resource.ResourceKey;

class BaseArmor extends BaseItem implements Armor {

  private int defense;

  @Override
  public int getDefense() {
    return defense;
  }

  @Override
  public ResourceKey getDescription() {
    return ModelBundle.ITEM_DESCRIPTION.get("armor", defense);
  }
}
