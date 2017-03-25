package com.jingyuyao.tactical.model.map.event;

import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.event.ObjectEvent;

public class SpawnCharacter extends ObjectEvent<Character> {

  public SpawnCharacter(Character object) {
    super(object);
  }
}
