package com.jingyuyao.tactical.model.item;

/**
 * The most basic thing a {@link com.jingyuyao.tactical.model.character.Character} could own.
 */
// TODO: should probably add a constant like INFINITE for items that doesn't expire.
public interface Item {

  String getName();

  int getUsageLeft();

  void useOnce();
}
