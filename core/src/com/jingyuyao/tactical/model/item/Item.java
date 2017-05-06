package com.jingyuyao.tactical.model.item;

import com.jingyuyao.tactical.model.i18n.Message;

/**
 * The most basic thing a {@link com.jingyuyao.tactical.model.character.Character} could own.
 */
// TODO: should probably add a constant like INFINITE for items that doesn't expire.
public interface Item {

  String getKey();

  Message getName();

  Message getDescription();

  int getUsageLeft();

  void useOnce();
}
