package com.jingyuyao.tactical.model.item;

import java.util.Locale;

class AbstractArmor extends AbstractItem implements Armor {

  private int defense;

  @Override
  public int getDefense() {
    return defense;
  }

  @Override
  public String getDescription() {
    return String.format(Locale.US, "%d defense", defense);
  }
}
