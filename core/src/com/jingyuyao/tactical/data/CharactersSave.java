package com.jingyuyao.tactical.data;

import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.map.Coordinate;
import java.util.Map;

class CharactersSave {

  private final Map<Coordinate, Character> characters;

  CharactersSave(Map<Coordinate, Character> characters) {
    this.characters = characters;
  }

  Map<Coordinate, Character> getCharacters() {
    return characters;
  }
}
