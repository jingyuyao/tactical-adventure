package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.world.Cell;
import javax.inject.Inject;

public class UsingConsumable extends BasePlayerState {

  private final Consumable consumable;

  @Inject
  UsingConsumable(
      ModelBus modelBus,
      WorldState worldState,
      StateFactory stateFactory,
      @Assisted Cell playerCell,
      @Assisted Consumable consumable) {
    super(modelBus, worldState, stateFactory, playerCell);
    this.consumable = consumable;
  }

  @Override
  public ImmutableList<Action> getActions() {
    return ImmutableList.of(
        new UseConsumableAction(this),
        new BackAction(this)
    );
  }

  public Consumable getConsumable() {
    return consumable;
  }

  void use() {
    consumable.apply(getPlayer());
    getPlayer().useConsumable(consumable);
    finish();
  }
}
