package com.jingyuyao.tactical.model.item;

import com.google.common.base.Preconditions;
import com.jingyuyao.tactical.model.resource.ModelBundle;
import com.jingyuyao.tactical.model.resource.ResourceKey;

/**
 * An {@link Item} that can be used and has a limited number of usages.
 */
class BaseItem implements Item {

  private String name;
  private int usageLeft;

  BaseItem() {
  }

  BaseItem(String name, int usageLeft) {
    this.name = name;
    this.usageLeft = usageLeft;
  }

  @Override
  public ResourceKey getAnimation() {
    return ModelBundle.WEAPON_ANIMATIONS.get(name);
  }

  @Override
  public ResourceKey getName() {
    return ModelBundle.ITEM_NAME.get(name);
  }

  @Override
  public ResourceKey getDescription() {
    return ModelBundle.ITEM_DESCRIPTION.get(name);
  }

  @Override
  public int getUsageLeft() {
    return usageLeft;
  }

  @Override
  public void useOnce() {
    Preconditions.checkState(usageLeft > 0);
    usageLeft--;
  }
}
