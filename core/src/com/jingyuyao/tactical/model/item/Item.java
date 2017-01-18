package com.jingyuyao.tactical.model.item;

/**
 * The most basic thing a {@link com.jingyuyao.tactical.model.character.Character} could own.
 */
// TODO: should probably add a constant like INFINITE for items that doesn't expire.
public interface Item {

  /**
   * The name of this item. Usually used for display purposes.
   */
  String getName();

  /**
   * Return the number of times this item can still be used.
   */
  int getUsageLeft();
}
