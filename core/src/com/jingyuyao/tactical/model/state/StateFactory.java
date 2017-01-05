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

  ReviewingAttack createReviewingAttack(Player player, AttackPlan attackPlan);

  SelectingWeapon createSelectingWeapon(Player player, Enemy enemy);
}
