package com.jingyuyao.tactical.model.item;

import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.i18n.Message;
import com.jingyuyao.tactical.model.i18n.ModelBundle;

public class Heal extends BaseItem implements Consumable {

  private int amount;

  Heal() {
  }

  Heal(int amount) {
    this.amount = amount;
  }

  @Override
  public Message getDescription() {
    return ModelBundle.ITEM_DESCRIPTION.get("heal", amount);
  }

  @Override
  public void apply(Character character) {
    character.healBy(amount);
  }
}
