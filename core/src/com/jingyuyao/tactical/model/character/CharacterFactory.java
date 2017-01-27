package com.jingyuyao.tactical.model.character;

import com.jingyuyao.tactical.model.map.Coordinate;

public interface CharacterFactory {

  Player createPlayer(Coordinate coordinate, CharacterData characterData);

  PassiveEnemy createPassiveEnemy(Coordinate coordinate, CharacterData characterData);
}
