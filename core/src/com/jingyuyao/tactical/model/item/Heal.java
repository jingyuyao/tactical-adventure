package com.jingyuyao.tactical.model.item;

import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.Character;
import javax.inject.Inject;

public class Heal extends BaseItem<HealData> implements Consumable {

  @Inject
  Heal(@Assisted Character character, @Assisted HealData healData) {
    super(character, healData);
  }

  @Override
  public void consume() {
    useOnce();
    getOwner().healBy(getData().getAmount());
  }
}
