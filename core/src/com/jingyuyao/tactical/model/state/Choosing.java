package com.jingyuyao.tactical.model.state;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.Player;
import javax.inject.Inject;

class Choosing extends AbstractPlayerState {

  @Inject
  Choosing(
      EventBus eventBus, MapState mapState, StateFactory stateFactory, @Assisted Player player) {
    super(eventBus, mapState, stateFactory, player);
  }

  @Override
  public void select(Player player) {
    if (Objects.equal(getPlayer(), player)) {
      back();
    } else {
      rollback();
      goTo(getStateFactory().createMoving(player));
    }
  }

  @Override
  public ImmutableList<Action> getActions() {
    ImmutableList.Builder<Action> builder = new ImmutableList.Builder<Action>();
    if (!Iterables.isEmpty(getPlayer().getWeapons())) {
      builder.add(this.new SelectWeapons());
    }
    if (!Iterables.isEmpty(getPlayer().getConsumables())) {
      builder.add(this.new UseItems());
    }
    builder.add(this.new Wait());
    builder.add(this.new Back());
    return builder.build();
  }

  class SelectWeapons implements Action {

    @Override
    public String getName() {
      return "weapons";
    }

    @Override
    public void run() {
      goTo(getStateFactory().createSelectingWeapon(getPlayer()));
    }
  }

  class UseItems implements Action {

    @Override
    public String getName() {
      return "items";
    }

    @Override
    public void run() {
      goTo(getStateFactory().createUsingItem(getPlayer()));
    }
  }

  class Wait implements Action {

    @Override
    public String getName() {
      return "wait";
    }

    @Override
    public void run() {
      finish(getPlayer());
    }
  }
}
