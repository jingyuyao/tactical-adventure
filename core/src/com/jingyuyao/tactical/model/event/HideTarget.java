package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.item.Target;

public class HideTarget extends ObjectEvent<Target> {

  public HideTarget(Target object) {
    super(object);
  }
}
