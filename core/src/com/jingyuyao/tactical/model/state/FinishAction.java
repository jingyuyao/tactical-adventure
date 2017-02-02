package com.jingyuyao.tactical.model.state;

class FinishAction implements Action {

  private AbstractPlayerState playerState;

  FinishAction(AbstractPlayerState playerState) {
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
