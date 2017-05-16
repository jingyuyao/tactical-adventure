package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.character.Ship;

public class RemoveCharacter extends ObjectEvent<Ship> {

  public RemoveCharacter(Ship object) {
    super(object);
  }
}
