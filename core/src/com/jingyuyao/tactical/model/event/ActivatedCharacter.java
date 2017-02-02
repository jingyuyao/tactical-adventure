package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.character.Character;

public class ActivatedCharacter extends ObjectEvent<Character> {

  public ActivatedCharacter(Character object) {
    super(object);
  }
}
