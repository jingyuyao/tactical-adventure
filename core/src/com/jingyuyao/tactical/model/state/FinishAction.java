package com.jingyuyao.tactical.model.state;

class FinishAction implements Action {

  private BasePlayerState playerState;

  FinishAction(BasePlayerState playerState) {
    this.playerState = playerState;
  }

  @Override
  public String getText() {
    return "finish";
  }

  @Override
  public void run() {
    playerState.finish();
  }
}
