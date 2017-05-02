package com.jingyuyao.tactical.model.character;

import com.google.common.collect.FluentIterable;
import com.jingyuyao.tactical.model.item.Item;

public interface Character {

  String getName();

  int getHp();

  int getMoveDistance();

  void damageBy(int delta);

  void healBy(int delta);

  void fullHeal();

  FluentIterable<Item> fluentItems();

  void useItem(Item item);
}
