package com.jingyuyao.tactical.data;

import com.jingyuyao.tactical.model.character.Character;
import java.util.List;

class MapSave {

  private List<Character> characters;

  MapSave(List<Character> characters) {
    this.characters = characters;
  }

  List<Character> getCharacters() {
    return characters;
  }
}
