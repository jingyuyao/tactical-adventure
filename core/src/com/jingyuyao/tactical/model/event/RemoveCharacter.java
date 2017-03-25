package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.character.Character;

public class RemoveCharacter extends ObjectEvent<Character> {

  public RemoveCharacter(Character object) {
    super(object);
  }
}
