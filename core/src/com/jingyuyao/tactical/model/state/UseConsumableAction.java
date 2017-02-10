package com.jingyuyao.tactical.model.state;

class UseConsumableAction implements Action {

  private final UsingConsumable usingConsumable;

  UseConsumableAction(UsingConsumable usingConsumable) {
    this.usingConsumable = usingConsumable;
  }

  @Override
  public String getName() {
    return "use";
  }

  @Override
  public void run() {
    usingConsumable.use();
  }
}
