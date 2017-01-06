package com.jingyuyao.tactical.model.character.event;

import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.event.AbstractEvent;

public class RemoveCharacter extends AbstractEvent<Character> {

  public RemoveCharacter(Character object) {
    super(object);
  }
}
