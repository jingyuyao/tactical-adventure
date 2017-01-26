package com.jingyuyao.tactical.model.character;

import com.jingyuyao.tactical.model.map.Coordinate;

public interface CharacterFactory {

  Player createPlayer(Coordinate coordinate, Stats stats);

  PassiveEnemy createPassiveEnemy(Coordinate coordinate, Stats stats);
}
