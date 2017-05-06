package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.i18n.Message;
import com.jingyuyao.tactical.model.i18n.ModelBundle;

class FinishAction implements Action {

  private final BasePlayerState playerState;

  FinishAction(BasePlayerState playerState) {
    this.playerState = playerState;
  }

  @Override
  public Message getMessage() {
    return ModelBundle.ACTION.get("finish");
  }

  @Override
  public void run() {
    playerState.finish();
  }
}
