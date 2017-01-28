package com.jingyuyao.tactical.model.character;

import com.jingyuyao.tactical.model.item.Item;
import java.util.List;

public interface CharacterFactory {

  BasePlayer createBasePlayer(PlayerData data, List<Item> items);

  PassiveEnemy createPassiveEnemy(CharacterData data, List<Item> items);
}
