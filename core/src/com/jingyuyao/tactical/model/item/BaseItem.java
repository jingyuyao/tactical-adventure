package com.jingyuyao.tactical.model.item;

import com.google.common.base.Preconditions;
import com.jingyuyao.tactical.model.resource.ModelBundle;
import com.jingyuyao.tactical.model.resource.ResourceKey;

/**
 * An {@link Item} that can be used and has a limited number of usages.
 */
class BaseItem implements Item {

  private String nameKey;
  private String resourceKey;
  private int usageLeft;

  BaseItem() {
  }

  BaseItem(String nameKey, String resourceKey, int usageLeft) {
    this.nameKey = nameKey;
    this.resourceKey = resourceKey;
    this.usageLeft = usageLeft;
  }

  @Override
  public String getResourceKey() {
    return resourceKey;
  }

  @Override
  public ResourceKey getName() {
    return ModelBundle.ITEM_NAME.get(nameKey);
  }

  @Override
  public ResourceKey getDescription() {
    return ModelBundle.ITEM_DESCRIPTION.get(nameKey);
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
