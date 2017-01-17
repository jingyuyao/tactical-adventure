package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.battle.Target;
import com.jingyuyao.tactical.model.character.Player;

/**
 * Factory class for all {@link State}. God bless {@link com.google.inject.assistedinject}.
 */
interface StateFactory {

  Waiting createWaiting();

  Moving createMoving(Player player);

  Moved createMoved(Player player);

  SelectingItem createSelectingItem(Player player);

  SelectingWeapon createSelectingWeapon(Player player);

  SelectingTarget createSelectingTarget(Player player, ImmutableList<Target> targets);

  ReviewingAttack createReviewingAttack(Player player, Target target);

  Retaliating createRetaliating();
}
