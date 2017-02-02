package com.jingyuyao.tactical.model.state;

class BackAction implements Action {

  private final BaseState baseState;

  BackAction(BaseState baseState) {
    this.baseState = baseState;
  }

  @Override
  public String getText() {
    return "back";
  }

  @Override
  public void run() {
    baseState.back();
  }
}
