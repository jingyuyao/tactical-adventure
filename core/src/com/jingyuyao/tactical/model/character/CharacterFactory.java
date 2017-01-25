package com.jingyuyao.tactical.model.character;

import com.google.inject.name.Named;
import com.jingyuyao.tactical.model.common.Coordinate;

public interface CharacterFactory {

  Player createPlayer(Coordinate coordinate, Stats stats);

  @Named("PassiveEnemy")
  Enemy createPassiveEnemy(Coordinate coordinate, Stats stats);
}
