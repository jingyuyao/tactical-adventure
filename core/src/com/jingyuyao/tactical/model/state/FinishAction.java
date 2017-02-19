package com.jingyuyao.tactical.model.state;

class FinishAction implements Action {

  private final BasePlayerState playerState;

  FinishAction(BasePlayerState playerState) {
    this.playerState = playerState;
  }

  @Override
  public String getName() {
    return "finish";
  }

  @Override
  public void run() {
    playerState.finish();
  }
}
