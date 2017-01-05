package com.jingyuyao.tactical.model.state.event;

import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.event.ObjectEvent;

public class HighlightCharacter extends ObjectEvent<Character> {

  public HighlightCharacter(Character character) {
    super(character);
  }
}
