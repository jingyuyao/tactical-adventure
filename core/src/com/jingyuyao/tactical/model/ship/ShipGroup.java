package com.jingyuyao.tactical.model.ship;

import com.google.common.base.Objects;
import java.io.Serializable;

public class ShipGroup implements Serializable {

  public static final ShipGroup PLAYER = new ShipGroup("player");
  public static final ShipGroup ENEMY = new ShipGroup("enemy");

  private String name;

  ShipGroup() {
  }

  public ShipGroup(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    ShipGroup shipGroup = (ShipGroup) object;
    return Objects.equal(name, shipGroup.name);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name);
  }
}
