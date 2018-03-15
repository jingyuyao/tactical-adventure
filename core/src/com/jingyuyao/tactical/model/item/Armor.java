package com.jingyuyao.tactical.model.item;

import com.jingyuyao.tactical.model.resource.ModelBundle;
import com.jingyuyao.tactical.model.resource.StringKey;

/**
 * Most basic type of ship armor, usually have a high number of usage and a fair defense.
 */
public class Armor extends Item {

  private int defense;

  Armor() {
  }

  @Override
  public StringKey getDescription() {
    return ModelBundle.ITEM_DESCRIPTION.get("armor", defense);
  }

  public int getDefense() {
    return defense;
  }
}
