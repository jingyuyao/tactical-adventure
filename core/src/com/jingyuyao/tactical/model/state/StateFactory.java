package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.AttackPlan;
import com.jingyuyao.tactical.model.battle.Target;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.Weapon;

/**
 * Factory class for all {@link State}. God bless {@link com.google.inject.assistedinject}.
 */
interface StateFactory {

  Waiting createWaiting();

  Choosing createChoosing(Player player);

  UsingItem createUsingItem(Player player);

  Moving createMoving(Player player);

  SelectingWeapon createSelectingWeapon(Player player);

  SelectingTarget createSelectingTarget(Weapon weapon, ImmutableList<Target> targets);

  ReviewingAttack createReviewingAttack(Player player, Enemy enemy, AttackPlan attackPlan);

  Retaliating createRetaliating();
}
