package com.jingyuyao.tactical.model.item;

public class BasicArmor extends BaseItem implements Armor {

  private int defense;

  @Override
  public int getDefense() {
    return defense;
  }

  @Override
  public String getDescription() {
    return "Basic armor that provides defense";
  }
}
