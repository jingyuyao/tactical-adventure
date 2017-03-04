package com.jingyuyao.tactical.data;

import com.google.common.collect.FluentIterable;
import com.jingyuyao.tactical.model.character.Character;
import java.util.List;

class CharactersSave {

  private final List<Character> characters;

  CharactersSave(List<Character> characters) {
    this.characters = characters;
  }

  FluentIterable<Character> getCharacters() {
    return FluentIterable.from(characters);
  }
}
