package com.jingyuyao.tactical.model.item;

import com.jingyuyao.tactical.model.resource.ResourceKey;
import com.jingyuyao.tactical.model.ship.Ship;

/**
 * The most basic thing a {@link Ship} could own.
 */
// TODO: should probably add a constant like INFINITE for items that doesn't expire.
public interface Item {

  String getResourceKey();

  ResourceKey getName();

  ResourceKey getDescription();

  int getUsageLeft();

  void useOnce();
}
