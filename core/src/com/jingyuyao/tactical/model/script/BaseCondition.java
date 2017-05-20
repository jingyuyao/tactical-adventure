package com.jingyuyao.tactical.model.script;

import com.jingyuyao.tactical.model.state.Turn;
import com.jingyuyao.tactical.model.world.World;

class BaseCondition implements Condition {

  private boolean triggered;

  @Override
  public boolean isMet(Turn turn, World world) {
    return false;
  }

  @Override
  public boolean isTriggered() {
    return triggered;
  }

  @Override
  public void triggered() {
    triggered = true;
  }
}
