package com.jingyuyao.tactical.model.item;

import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.Character;
import javax.inject.Inject;

public class Heal extends BaseItem<ItemData> implements Consumable {

  // TODO: Remove me
  static final int AMOUNT = 10;

  @Inject
  Heal(@Assisted Character character, @Assisted ItemData itemData) {
    super(character, itemData);
  }

  @Override
  public void consume() {
    useOnce();
    getOwner().healBy(AMOUNT);
  }
}
