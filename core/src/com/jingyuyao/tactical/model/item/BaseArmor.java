package com.jingyuyao.tactical.model.item;

import com.jingyuyao.tactical.model.i18n.Message;
import com.jingyuyao.tactical.model.i18n.ModelBundle;

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
