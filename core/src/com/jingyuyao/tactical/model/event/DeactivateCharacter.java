package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.character.Character;

public class DeactivateCharacter extends ObjectEvent<Character> {

  public DeactivateCharacter(Character object) {
    super(object);
  }
}
