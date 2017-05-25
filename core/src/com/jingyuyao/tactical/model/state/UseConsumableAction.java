package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.resource.ModelBundle;
import com.jingyuyao.tactical.model.resource.StringKey;

class UseConsumableAction implements Action {

  private final UsingConsumable usingConsumable;

  UseConsumableAction(UsingConsumable usingConsumable) {
    this.usingConsumable = usingConsumable;
  }

  @Override
  public StringKey getText() {
    return ModelBundle.ACTION.get("use");
  }

  @Override
  public void run() {
    usingConsumable.use();
  }
}
