package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.resource.ModelBundle;
import com.jingyuyao.tactical.model.resource.StringKey;

class FinishAction implements Action {

  private final ControllingState playerState;

  FinishAction(ControllingState playerState) {
    this.playerState = playerState;
  }

  @Override
  public StringKey getText() {
    return ModelBundle.ACTION.get("finish");
  }

  @Override
  public void run() {
    playerState.finish();
  }
}
