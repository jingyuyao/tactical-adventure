package com.jingyuyao.tactical.model.item;

import com.jingyuyao.tactical.model.resource.Message;
import com.jingyuyao.tactical.model.resource.ModelBundle;

class BaseArmor extends BaseItem implements Armor {

  private int defense;

  @Override
  public int getDefense() {
    return defense;
  }

  @Override
  public Message getDescription() {
    return ModelBundle.ITEM_DESCRIPTION.get("armor", defense);
  }
}
