package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.AttackPlan;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;

/**
 * Factory class for all {@link State}. God bless {@link com.google.inject.assistedinject}.
 */
interface StateFactory {

  Waiting createWaiting();

  Choosing createChoosing(Player player);

  UsingItem createUsingItem(Player player);

  Moving createMoving(Player player);

  SelectingWeapon createSelectingWeapon(Player player, Enemy enemy);

  ReviewingAttack createReviewingAttack(Player player, Enemy enemy, AttackPlan attackPlan);

  Retaliating createRetaliating();
}
