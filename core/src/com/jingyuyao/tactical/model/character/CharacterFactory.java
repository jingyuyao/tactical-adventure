package com.jingyuyao.tactical.model.character;

import com.google.inject.name.Named;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.item.Item;
import java.util.List;

public interface CharacterFactory {

  Player createPlayer(Coordinate coordinate, Stats stats, List<Item> items);

  @Named("PassiveEnemy")
  Enemy createPassiveEnemy(Coordinate coordinate, Stats stats, List<Item> items);
}
