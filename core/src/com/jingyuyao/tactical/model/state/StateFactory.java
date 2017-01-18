package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.Movement;
import com.jingyuyao.tactical.model.target.Target;

/**
 * Factory class for all {@link State}. God bless {@link com.google.inject.assistedinject}.
 */
interface StateFactory {

  Waiting createWaiting();

  Moving createMoving(Player player, Movement movement);

  Moved createMoved(Player player);

  SelectingItem createSelectingItem(Player player);

  SelectingTarget createSelectingTarget(
      Player player, Weapon weapon, ImmutableList<Target> targets);

  ReviewingAttack createReviewingAttack(Player player, Weapon weapon, Target target);

  Retaliating createRetaliating();
}
