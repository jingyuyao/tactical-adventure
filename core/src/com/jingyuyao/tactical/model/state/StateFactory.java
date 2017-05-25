package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.battle.Battle;
import com.jingyuyao.tactical.model.battle.Target;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Movement;
import java.util.List;

/**
 * Factory class for all {@link State}. God bless {@link com.google.inject.assistedinject}.
 */
interface StateFactory {

  Transition createTransition();

  Waiting createWaiting();

  StartTurn createStartTurn();

  EndTurn createEndTurn();

  Moving createMoving(Cell cell, Movement movement);

  Moved createMoved(Cell cell);

  SelectingTarget createSelectingTarget(Cell cell, Weapon weapon, List<Target> targets);

  UsingConsumable createUsingConsumable(Cell cell, Consumable consumable);

  Battling createBattling(Cell cell, Battle battle);

  Retaliating createRetaliating();
}
