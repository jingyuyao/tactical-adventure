package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.item.Target;

public class ShowTarget extends ObjectEvent<Target> {

  public ShowTarget(Target object) {
    super(object);
  }
}
