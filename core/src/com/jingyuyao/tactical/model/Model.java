package com.jingyuyao.tactical.model;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.ModelModule.ModelEventBus;
import com.jingyuyao.tactical.model.event.SelectCell;
import com.jingyuyao.tactical.model.state.State;
import com.jingyuyao.tactical.model.state.WorldState;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.World;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Model {

  private final World world;
  private final WorldState worldState;
  private final EventBus eventBus;

  @Inject
  Model(World world, WorldState worldState, @ModelEventBus EventBus eventBus) {
    this.world = world;
    this.worldState = worldState;
    this.eventBus = eventBus;
  }

  public void load(Iterable<Cell> cells, State initialState) {
    world.load(cells);
    worldState.initialize(initialState);
  }

  public void prepForSave() {
    worldState.prepForSave();
  }

  public void reset() {
    world.reset();
    worldState.reset();
  }

  public void select(Cell cell) {
    eventBus.post(new SelectCell(cell));
    worldState.select(cell);
  }
}
