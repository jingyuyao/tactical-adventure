package com.jingyuyao.tactical.model.character;

import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.item.Item;
import java.util.List;

public interface CharacterFactory {

  Player createPlayer(Coordinate coordinate, Stats stats, Items items);

  Enemy createEnemy(Coordinate coordinate, Stats stats, Items items);

  Items createItems(List<Item> items);
}
