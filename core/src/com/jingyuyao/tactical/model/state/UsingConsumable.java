package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.ModelModule.ModelEventBus;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.Consumable;
import javax.inject.Inject;

public class UsingConsumable extends AbstractPlayerState {

  private final Consumable consumable;

  @Inject
  UsingConsumable(
      @ModelEventBus EventBus eventBus,
      MapState mapState,
      StateFactory stateFactory,
      @Assisted Player player,
      @Assisted Consumable consumable) {
    super(eventBus, mapState, stateFactory, player);
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
    getPlayer().quickAccess(consumable);
    consumable.apply(getPlayer());
    getPlayer().useItem(consumable);
    finish();
  }
}
