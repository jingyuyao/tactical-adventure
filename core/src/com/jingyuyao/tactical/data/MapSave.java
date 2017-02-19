package com.jingyuyao.tactical.data;

import com.google.common.collect.FluentIterable;
import com.jingyuyao.tactical.model.character.Character;
import java.util.List;

class MapSave {

  private final List<Character> characters;

  MapSave(List<Character> characters) {
    this.characters = characters;
  }

  FluentIterable<Character> getCharacters() {
    return FluentIterable.from(characters);
  }
}
