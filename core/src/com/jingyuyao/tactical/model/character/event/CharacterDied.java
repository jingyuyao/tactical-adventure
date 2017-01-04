package com.jingyuyao.tactical.model.character.event;

import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.event.ObjectEvent;

public class CharacterDied extends ObjectEvent<Character> {
  public CharacterDied(Character object) {
    super(object);
  }
}
