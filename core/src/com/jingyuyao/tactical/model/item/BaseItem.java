package com.jingyuyao.tactical.model.item;

import com.google.common.base.Preconditions;
import com.jingyuyao.tactical.model.i18n.Message;
import com.jingyuyao.tactical.model.i18n.MessageBundle;

/**
 * An {@link Item} that can be used and has a limited number of usages.
 */
class BaseItem implements Item {

  private String key;
  private int usageLeft;

  BaseItem() {
  }

  BaseItem(String key, int usageLeft) {
    this.key = key;
    this.usageLeft = usageLeft;
  }

  @Override
  public String getKey() {
    return key;
  }

  @Override
  public Message getName() {
    return MessageBundle.ITEM_NAME.get(key);
  }

  @Override
  public Message getDescription() {
    return MessageBundle.ITEM_DESCRIPTION.get(key);
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
