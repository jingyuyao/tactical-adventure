package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.i18n.Message;
import com.jingyuyao.tactical.model.i18n.MessageBundle;

class BackAction implements Action {

  private final BaseState baseState;

  BackAction(BaseState baseState) {
    this.baseState = baseState;
  }

  @Override
  public Message getMessage() {
    return MessageBundle.ACTION.get("back");
  }

  @Override
  public String getName() {
    return "back";
  }

  @Override
  public void run() {
    baseState.back();
  }
}
