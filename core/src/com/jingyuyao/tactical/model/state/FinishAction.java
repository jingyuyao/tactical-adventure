package com.jingyuyao.tactical.model.state;

class FinishAction implements Action {

  private final AbstractPlayerState playerState;

  FinishAction(AbstractPlayerState playerState) {
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
