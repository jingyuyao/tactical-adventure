package com.jingyuyao.tactical.model.item;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.common.EventBusObject;

/**
 * Super class of all the items in the game.
 */
public class Item extends EventBusObject {

  private final String name;

  Item(EventBus eventBus, String name) {
    super(eventBus);
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
