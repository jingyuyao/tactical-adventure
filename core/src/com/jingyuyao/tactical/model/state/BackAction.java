package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.resource.ModelBundle;
import com.jingyuyao.tactical.model.resource.ResourceKey;

class BackAction implements Action {

  private final BaseState baseState;

  BackAction(BaseState baseState) {
    this.baseState = baseState;
  }

  @Override
  public ResourceKey getText() {
    return ModelBundle.ACTION.get("back");
  }

  @Override
  public void run() {
    baseState.back();
  }
}
