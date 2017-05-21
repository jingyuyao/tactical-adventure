package com.jingyuyao.tactical.model.script;

import com.google.common.base.Objects;
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

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    BaseCondition that = (BaseCondition) object;
    return isTriggered() == that.isTriggered();
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(isTriggered());
  }
}
