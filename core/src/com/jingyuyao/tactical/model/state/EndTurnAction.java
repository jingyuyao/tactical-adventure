package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.i18n.Message;
import com.jingyuyao.tactical.model.i18n.ModelBundle;

class EndTurnAction implements Action {

  private final Waiting waiting;

  EndTurnAction(Waiting waiting) {
    this.waiting = waiting;
  }

  @Override
  public Message getMessage() {
    return ModelBundle.ACTION.get("end");
  }

  @Override
  public void run() {
    waiting.endTurn();
  }
}
