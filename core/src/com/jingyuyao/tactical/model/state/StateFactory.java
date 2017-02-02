package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.Movement;

/**
 * Factory class for all {@link State}. God bless {@link com.google.inject.assistedinject}.
 */
interface StateFactory {

  Transition createTransition();

  Waiting createWaiting();

  Moving createMoving(Player player, Movement movement);

  Moved createMoved(Player player);

  SelectingTarget createSelectingTarget(
      Player player, Weapon weapon, ImmutableList<Target> targets);

  ReviewingAttack createReviewingAttack(Player player, Weapon weapon, Target target);

  Retaliating createRetaliating();
}
