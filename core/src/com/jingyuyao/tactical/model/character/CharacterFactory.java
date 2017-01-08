package com.jingyuyao.tactical.model.character;

import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Weapon;
import java.util.List;

public interface CharacterFactory {

  Player createPlayer(Coordinate coordinate, String name, Stats stats, Items items);

  Enemy createEnemy(Coordinate coordinate, String name, Stats stats, Items items);

  Items createItems(List<Weapon> weapons, List<Consumable> consumables);
}
