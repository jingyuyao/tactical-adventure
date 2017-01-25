package com.jingyuyao.tactical.model.character;

import com.jingyuyao.tactical.model.common.Coordinate;

public interface CharacterFactory {

  Player createPlayer(Coordinate coordinate, Stats stats);

  PassiveEnemy createPassiveEnemy(Coordinate coordinate, Stats stats);
}
