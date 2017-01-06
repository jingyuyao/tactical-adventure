package com.jingyuyao.tactical.model.item.event;

import com.jingyuyao.tactical.model.event.AbstractEvent;
import com.jingyuyao.tactical.model.item.Item;

public class RemoveItem extends AbstractEvent<Item> {

  public RemoveItem(Item object) {
    super(object);
  }
}
