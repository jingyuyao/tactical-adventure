package com.jingyuyao.tactical.model.item;

import com.jingyuyao.tactical.model.i18n.Message;
import com.jingyuyao.tactical.model.ship.Ship;

/**
 * The most basic thing a {@link Ship} could own.
 */
// TODO: should probably add a constant like INFINITE for items that doesn't expire.
public interface Item {

  String getResourceKey();

  Message getName();

  Message getDescription();

  int getUsageLeft();

  void useOnce();
}
