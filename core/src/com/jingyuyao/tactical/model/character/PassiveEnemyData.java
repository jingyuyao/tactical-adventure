package com.jingyuyao.tactical.model.character;

import com.jingyuyao.tactical.model.item.Item;
import java.util.List;

public class PassiveEnemyData extends CharacterData {

  @Override
  public Character load(CharacterFactory factory, List<Item> items) {
    return factory.create(this, items);
  }
}
