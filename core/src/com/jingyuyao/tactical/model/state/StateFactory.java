package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.target.Target;

/**
 * Factory class for all {@link State}. God bless {@link com.google.inject.assistedinject}.
 */
interface StateFactory {

  Waiting createWaiting();

  Moving createMoving(Player player);

  Moved createMoved(Player player);

  SelectingItem createSelectingItem(Player player);

  SelectingTarget createSelectingTarget(Player player, ImmutableList<Target> targets);

  ReviewingAttack createReviewingAttack(Player player, Target target);

  Retaliating createRetaliating();
}
