package com.jingyuyao.tactical.model.state;

import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.world.Cell;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;

public class UsingConsumable extends ControllingState {

  private final Consumable consumable;

  @Inject
  UsingConsumable(
      ModelBus modelBus,
      WorldState worldState,
      StateFactory stateFactory,
      @Assisted Cell cell,
      @Assisted Consumable consumable) {
    super(modelBus, worldState, stateFactory, cell);
    this.consumable = consumable;
  }

  @Override
  public List<Action> getActions() {
    return Arrays.asList(
        new UseConsumableAction(this),
        new BackAction(this)
    );
  }

  public Consumable getConsumable() {
    return consumable;
  }

  void use() {
    consumable.apply(getShip());
    getShip().useConsumable(consumable);
    finish();
  }
}
