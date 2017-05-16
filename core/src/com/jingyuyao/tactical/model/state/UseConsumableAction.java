package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.resource.Message;
import com.jingyuyao.tactical.model.resource.ModelBundle;

class UseConsumableAction implements Action {

  private final UsingConsumable usingConsumable;

  UseConsumableAction(UsingConsumable usingConsumable) {
    this.usingConsumable = usingConsumable;
  }

  @Override
  public Message getMessage() {
    return ModelBundle.ACTION.get("use");
  }

  @Override
  public void run() {
    usingConsumable.use();
  }
}
