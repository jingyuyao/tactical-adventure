package com.jingyuyao.tactical.model.state.event;

import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.event.AbstractEvent;

public class HighlightCharacter extends AbstractEvent<Character> {

  public HighlightCharacter(Character character) {
    super(character);
  }
}
