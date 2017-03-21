package com.jingyuyao.tactical.data;

import com.jingyuyao.tactical.model.character.Character;
import java.util.List;

class CharactersSave {

  private final List<Character> characters;

  CharactersSave(List<Character> characters) {
    this.characters = characters;
  }

  Iterable<Character> getCharacters() {
    return characters;
  }
}
