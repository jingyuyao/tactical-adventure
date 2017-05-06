package com.jingyuyao.tactical.model.item;

import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.i18n.Message;
import com.jingyuyao.tactical.model.i18n.MessageBundle;

public class Heal extends BaseItem implements Consumable {

  private int amount;

  Heal() {
  }

  Heal(String name, int usageLeft, int amount) {
    super(name, usageLeft);
    this.amount = amount;
  }

  @Override
  public Message getDescription() {
    return MessageBundle.ITEM_DESCRIPTION.get("heal", amount);
  }

  @Override
  public void apply(Character character) {
    character.healBy(amount);
  }
}
