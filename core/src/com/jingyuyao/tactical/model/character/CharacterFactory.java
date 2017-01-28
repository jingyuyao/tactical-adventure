package com.jingyuyao.tactical.model.character;

import com.jingyuyao.tactical.model.item.Item;
import java.util.List;

public interface CharacterFactory {

  BasePlayer create(BasePlayerData data, List<Item> items);

  PassiveEnemy create(PassiveEnemyData data, List<Item> items);
}
