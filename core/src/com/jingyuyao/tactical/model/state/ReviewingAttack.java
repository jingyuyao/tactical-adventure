package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.battle.Target;
import com.jingyuyao.tactical.model.character.Player;
import javax.inject.Inject;

class ReviewingAttack extends AbstractPlayerState {

  private final Target target;

  @Inject
  ReviewingAttack(
      EventBus eventBus,
      MapState mapState,
      StateFactory stateFactory,
      @Assisted Player player,
      @Assisted Target target) {
    super(eventBus, mapState, stateFactory, player);
    this.target = target;
  }

  @Override
  public void enter() {
    target.showMarking();
  }

  @Override
  public void exit() {
    target.hideMarking();
  }

  @Override
  public ImmutableList<Action> getActions() {
    return ImmutableList.of(this.new Attack(), this.new Back());
  }

  class Attack implements Action {

    @Override
    public String getName() {
      return "attack";
    }

    @Override
    public void run() {
      target.execute();
      finish();
    }
  }
}
