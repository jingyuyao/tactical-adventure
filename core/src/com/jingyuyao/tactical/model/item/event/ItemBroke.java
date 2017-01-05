package com.jingyuyao.tactical.model.item.event;

import com.jingyuyao.tactical.model.event.ObjectEvent;
import com.jingyuyao.tactical.model.item.Item;

public class ItemBroke extends ObjectEvent<Item> {

  public ItemBroke(Item object) {
    super(object);
  }
}
