package com.jingyuyao.tactical.model.state;

class EndTurnAction implements Action {

  private final Waiting waiting;

  EndTurnAction(Waiting waiting) {
    this.waiting = waiting;
  }

  @Override
  public String getName() {
    return "end";
  }

  @Override
  public void run() {
    waiting.endTurn();
  }
}
