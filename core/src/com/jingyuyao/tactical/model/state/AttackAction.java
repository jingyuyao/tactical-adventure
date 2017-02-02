package com.jingyuyao.tactical.model.state;

class AttackAction implements Action {

  private ReviewingAttack reviewingAttack;

  AttackAction(ReviewingAttack reviewingAttack) {
    this.reviewingAttack = reviewingAttack;
  }

  @Override
  public String getText() {
    return "attack";
  }

  @Override
  public void run() {
    reviewingAttack.attack();
  }
}
