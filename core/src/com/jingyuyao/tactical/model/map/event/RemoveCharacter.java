package com.jingyuyao.tactical.model.map.event;

import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.event.ObjectEvent;

public class RemoveCharacter extends ObjectEvent<Character> {

  public RemoveCharacter(Character object) {
    super(object);
  }
}
