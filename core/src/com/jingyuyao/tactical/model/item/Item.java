package com.jingyuyao.tactical.model.item;

import com.google.common.base.Preconditions;
import com.jingyuyao.tactical.model.resource.ModelBundle;
import com.jingyuyao.tactical.model.resource.StringKey;
import com.jingyuyao.tactical.model.ship.Ship;
import java.io.Serializable;

/**
 * The most basic thing a {@link Ship} could own, has limited number of usages.
 */
public class Item implements Serializable {

  private String name;
  private int usageLeft;

  Item() {
  }

  Item(String name, int usageLeft) {
    this.name = name;
    this.usageLeft = usageLeft;
  }

  public StringKey getAnimation() {
    return ModelBundle.WEAPON_ANIMATIONS.get(name);
  }

  public StringKey getName() {
    return ModelBundle.ITEM_NAME.get(name);
  }

  public StringKey getDescription() {
    return ModelBundle.ITEM_DESCRIPTION.get(name);
  }

  public int getUsageLeft() {
    return usageLeft;
  }

  public void useOnce() {
    Preconditions.checkState(usageLeft > 0);
    usageLeft--;
  }
}
