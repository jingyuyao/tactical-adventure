package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.i18n.Message;
import com.jingyuyao.tactical.model.i18n.ModelBundle;

class BackAction implements Action {

  private final BaseState baseState;

  BackAction(BaseState baseState) {
    this.baseState = baseState;
  }

  @Override
  public Message getMessage() {
    return ModelBundle.ACTION.get("back");
  }

  @Override
  public void run() {
    baseState.back();
  }
}
