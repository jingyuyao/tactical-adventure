package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.map.Movement;

public class HideMovement extends ObjectEvent<Movement> {

  public HideMovement(Movement object) {
    super(object);
  }
}
