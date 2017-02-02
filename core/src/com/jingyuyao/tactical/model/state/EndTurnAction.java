package com.jingyuyao.tactical.model.state;

class EndTurnAction implements Action {

  private Waiting waiting;

  EndTurnAction(Waiting waiting) {
    this.waiting = waiting;
  }

  @Override
  public String getText() {
    return "end";
  }

  @Override
  public void run() {
    waiting.endTurn();
  }
}
