package com.jingyuyao.tactical.model.character.event;

import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.event.AbstractEvent;

public class CharacterDied extends AbstractEvent<Character> {

  public CharacterDied(Character object) {
    super(object);
  }
}
