package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.character.Player;

abstract class AbstractPlayerState extends AbstractState {

  private final Player player;

  AbstractPlayerState(
      EventBus eventBus, MapState mapState, StateFactory stateFactory, Player player) {
    super(eventBus, mapState, stateFactory);
    this.player = player;
  }

  Player getPlayer() {
    return player;
  }

  @Override
  public ImmutableList<Action> getActions() {
    ImmutableList.Builder<Action> builder = new ImmutableList.Builder<Action>();
    if (!Iterables.isEmpty(player.getWeapons())) {
      builder.add(this.new SelectWeapons());
    }
    if (!Iterables.isEmpty(player.getConsumables())) {
      builder.add(this.new SelectItems());
    }
    builder.add(this.new Wait());
    builder.add(this.new Back());
    return builder.build();
  }

  void finish() {
    player.setActionable(false);
    newWaitStack();
  }

  class SelectWeapons implements Action {

    @Override
    public String getName() {
      return "weapons";
    }

    @Override
    public void run() {
      goTo(getStateFactory().createSelectingWeapon(player));
    }
  }

  class SelectItems implements Action {

    @Override
    public String getName() {
      return "items";
    }

    @Override
    public void run() {
      goTo(getStateFactory().createSelectingItem(player));
    }
  }

  class Wait implements Action {

    @Override
    public String getName() {
      return "wait";
    }

    @Override
    public void run() {
      finish();
    }
  }
}
