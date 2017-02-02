package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.character.Character;

public class SelectCharacter extends ObjectEvent<Character> {

  public SelectCharacter(Character character) {
    super(character);
  }
}
