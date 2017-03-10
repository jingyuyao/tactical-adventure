package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.ModelModule.ModelEventBus;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.event.LevelComplete;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.map.Movements;
import javax.inject.Inject;

public class Waiting extends BaseState {

  private final StateFactory stateFactory;
  private final Characters characters;
  private final Movements movements;

  @Inject
  Waiting(
      @ModelEventBus EventBus eventBus,
      MapState mapState,
      StateFactory stateFactory,
      Characters characters,
      Movements movements) {
    super(eventBus, mapState);
    this.stateFactory = stateFactory;
    this.characters = characters;
    this.movements = movements;
  }

  @Override
  public void enter() {
    super.enter();
    if (Iterables.isEmpty(characters.fluent().filter(Enemy.class))) {
      post(new LevelComplete());
    }
  }

  @Override
  public void select(Player player) {
    if (player.isActionable()) {
      goTo(stateFactory.createMoving(player, movements.distanceFrom(player)));
    }
  }

  @Override
  public ImmutableList<Action> getActions() {
    return ImmutableList.<Action>of(new EndTurnAction(this));
  }

  void endTurn() {
    for (Player player : characters.fluent().filter(Player.class)) {
      player.setActionable(true);
    }
    goTo(stateFactory.createRetaliating());
  }
}
