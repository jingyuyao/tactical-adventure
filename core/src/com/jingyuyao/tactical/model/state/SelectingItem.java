package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.Consumable;
import java.util.Locale;
import javax.inject.Inject;

// TODO: Fire off an event so the UI show a widget that manages items
class SelectingItem extends AbstractPlayerState {

  @Inject
  SelectingItem(
      EventBus eventBus, MapState mapState, StateFactory stateFactory, @Assisted Player player) {
    super(eventBus, mapState, stateFactory, player);
  }

  @Override
  public ImmutableList<Action> getActions() {
    ImmutableList.Builder<Action> builder = new ImmutableList.Builder<Action>();
    for (Consumable consumable : getPlayer().getConsumables()) {
      builder.add(this.new UseConsumable(consumable));
    }
    builder.add(this.new Back());
    return builder.build();
  }

  class UseConsumable implements Action {

    private final Consumable consumable;

    UseConsumable(Consumable consumable) {
      this.consumable = consumable;
    }

    @Override
    public String getName() {
      return String.format(Locale.US, "%s (%d)", consumable.getName(), consumable.getUsageLeft());
    }

    @Override
    public void run() {
      consumable.consume(getPlayer());
      finish();
    }
  }
}