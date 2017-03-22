package com.jingyuyao.tactical.model.state;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.ModelModule.ModelEventBus;
import com.jingyuyao.tactical.model.World;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.event.LevelComplete;
import com.jingyuyao.tactical.model.event.LevelFailed;
import com.jingyuyao.tactical.model.map.Cell;
import com.jingyuyao.tactical.model.map.Movements;
import javax.inject.Inject;

public class Waiting extends BaseState {

  private final StateFactory stateFactory;
  private final World world;
  private final Movements movements;

  @Inject
  Waiting(
      @ModelEventBus EventBus eventBus,
      MapState mapState,
      StateFactory stateFactory,
      World world,
      Movements movements) {
    super(eventBus, mapState);
    this.stateFactory = stateFactory;
    this.world = world;
    this.movements = movements;
  }

  @Override
  public void enter() {
    super.enter();
    FluentIterable<Character> characters = world.getCharacters();
    if (Iterables.isEmpty(characters.filter(Player.class))) {
      post(new LevelFailed());
    }
    if (Iterables.isEmpty(characters.filter(Enemy.class))) {
      post(new LevelComplete());
    }
  }

  @Override
  public void select(Cell cell) {
    if (cell.hasPlayer()) {
      Player player = cell.getPlayer();
      if (player.isActionable()) {
        goTo(stateFactory.createMoving(player, movements.distanceFrom(player)));
      }
    }
  }

  @Override
  public ImmutableList<Action> getActions() {
    return ImmutableList.<Action>of(new EndTurnAction(this));
  }

  void endTurn() {
    for (Player player : world.getCharacters().filter(Player.class)) {
      player.setActionable(true);
    }
    goTo(stateFactory.createRetaliating());
  }
}
