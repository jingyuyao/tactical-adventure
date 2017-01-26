package com.jingyuyao.tactical.model.item;

import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.Character;
import javax.inject.Inject;

public class Heal extends BaseItem<ItemStats> implements Consumable {

  // TODO: Remove me
  static final int AMOUNT = 10;

  @Inject
  Heal(@Assisted Character character, @Assisted ItemStats itemStats) {
    super(character, itemStats);
  }

  @Override
  public void consume() {
    useOnce();
    getOwner().healBy(AMOUNT);
  }
}
