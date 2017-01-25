package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.character.Character;

public class HighlightCharacter extends AbstractEvent<Character> {

  public HighlightCharacter(Character character) {
    super(character);
  }
}
