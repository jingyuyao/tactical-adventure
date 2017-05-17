package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.resource.ModelBundle;
import com.jingyuyao.tactical.model.resource.ResourceKey;

class FinishAction implements Action {

  private final ControllingState playerState;

  FinishAction(ControllingState playerState) {
    this.playerState = playerState;
  }

  @Override
  public ResourceKey getText() {
    return ModelBundle.ACTION.get("finish");
  }

  @Override
  public void run() {
    playerState.finish();
  }
}
