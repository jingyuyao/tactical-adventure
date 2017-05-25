package com.jingyuyao.tactical.model.script;

import com.jingyuyao.tactical.model.ship.ShipGroup;
import java.io.Serializable;

public class DeactivateGroup implements Serializable {

  private ShipGroup group;

  DeactivateGroup() {
  }

  public ShipGroup getGroup() {
    return group;
  }
}
