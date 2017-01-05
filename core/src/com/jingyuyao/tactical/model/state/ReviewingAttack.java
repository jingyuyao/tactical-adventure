package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.AttackPlan;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.mark.Markings;
import com.jingyuyao.tactical.model.state.event.HideAttackPlan;
import com.jingyuyao.tactical.model.state.event.ShowAttackPlan;
import javax.inject.Inject;

class ReviewingAttack extends AbstractPlayerState {

  private final AttackPlan attackPlan;

  @Inject
  ReviewingAttack(
      EventBus eventBus,
      MapState mapState,
      Markings markings,
      StateFactory stateFactory,
      @Assisted Player player,
      @Assisted AttackPlan attackPlan) {
    super(eventBus, mapState, markings, stateFactory, player);
    this.attackPlan = attackPlan;
  }

  @Override
  public void enter() {
    super.enter();
    // TODO: use a different marker for each stage
    // TODO: show equipped weapon targets only
    getMarkings().showImmediateTargets(getPlayer());
    post(new ShowAttackPlan(attackPlan));
  }

  @Override
  public void exit() {
    post(new HideAttackPlan());
    super.exit();
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
      attackPlan.execute();
      finish(getPlayer());
    }
  }
}
