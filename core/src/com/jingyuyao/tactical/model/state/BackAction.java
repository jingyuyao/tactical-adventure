package com.jingyuyao.tactical.model.state;

class BackAction implements Action {

  private final AbstractState abstractState;

  BackAction(AbstractState abstractState) {
    this.abstractState = abstractState;
  }

  @Override
  public String getText() {
    return "back";
  }

  @Override
  public void run() {
    abstractState.back();
  }
}
