package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.resource.ModelBundle;
import com.jingyuyao.tactical.model.resource.ResourceKey;

class EndTurnAction implements Action {

  private final Waiting waiting;

  EndTurnAction(Waiting waiting) {
    this.waiting = waiting;
  }

  @Override
  public ResourceKey getText() {
    return ModelBundle.ACTION.get("end");
  }

  @Override
  public void run() {
    waiting.endTurn();
  }
}
