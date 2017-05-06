package com.jingyuyao.tactical.model.item;

import com.google.common.base.Preconditions;
import com.jingyuyao.tactical.model.i18n.Message;
import com.jingyuyao.tactical.model.i18n.MessageBundle;

/**
 * An {@link Item} that can be used and has a limited number of usages.
 */
class BaseItem implements Item {

  private String nameKey;
  private int usageLeft;

  BaseItem() {
  }

  BaseItem(String nameKey, int usageLeft) {
    this.nameKey = nameKey;
    this.usageLeft = usageLeft;
  }

  @Override
  public String getNameKey() {
    return nameKey;
  }

  @Override
  public Message getName() {
    return MessageBundle.ITEM_NAME.get(nameKey);
  }

  @Override
  public Message getDescription() {
    return MessageBundle.ITEM_DESCRIPTION.get(nameKey);
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
