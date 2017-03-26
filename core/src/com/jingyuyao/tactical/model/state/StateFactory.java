package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Movement;

/**
 * Factory class for all {@link State}. God bless {@link com.google.inject.assistedinject}.
 */
interface StateFactory {

  Transition createTransition();

  Waiting createWaiting();

  Moving createMoving(Cell cell, Movement movement);

  Moved createMoved(Cell cell);

  SelectingTarget createSelectingTarget(
      Player player, Weapon weapon, ImmutableList<Target> targets);

  UsingConsumable createUsingConsumable(Player player, Consumable consumable);

  Battling createBattling(Player player, Weapon weapon, Target target);

  Retaliating createRetaliating();
}
