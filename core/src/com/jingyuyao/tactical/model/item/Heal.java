package com.jingyuyao.tactical.model.item;

import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.Character;
import java.util.Locale;
import javax.inject.Inject;

public class Heal extends BaseItem<HealData> implements Consumable {

  @Inject
  Heal(@Assisted HealData healData) {
    super(healData);
  }

  @Override
  public void apply(Character character) {
    character.healBy(getData().getAmount());
  }

  @Override
  public String toString() {
    return String.format(
        Locale.US, "%s\nHeal(%d) Usg(%d)", getName(), getData().getAmount(), getUsageLeft());
  }
}
