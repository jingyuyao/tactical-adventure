package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.i18n.Message;
import com.jingyuyao.tactical.model.i18n.MessageBundle;

class FinishAction implements Action {

  private final BasePlayerState playerState;

  FinishAction(BasePlayerState playerState) {
    this.playerState = playerState;
  }

  @Override
  public Message getMessage() {
    return MessageBundle.ACTION.get("finish");
  }

  @Override
  public void run() {
    playerState.finish();
  }
}
