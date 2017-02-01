package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.map.Movement;

public class ShowMovement extends ObjectEvent<Movement> {

  public ShowMovement(Movement object) {
    super(object);
  }
}
