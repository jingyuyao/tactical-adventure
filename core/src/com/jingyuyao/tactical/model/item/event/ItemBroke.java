package com.jingyuyao.tactical.model.item.event;

import com.jingyuyao.tactical.model.event.AbstractEvent;
import com.jingyuyao.tactical.model.item.Item;

public class ItemBroke extends AbstractEvent<Item> {

  public ItemBroke(Item object) {
    super(object);
  }
}
