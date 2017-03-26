package com.jingyuyao.tactical.model;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.ModelModule.ModelEventBus;
import com.jingyuyao.tactical.model.event.SelectCell;
import com.jingyuyao.tactical.model.map.Cell;
import com.jingyuyao.tactical.model.state.MapState;
import com.jingyuyao.tactical.model.state.State;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Model {

  private final World world;
  private final MapState mapState;
  private final EventBus eventBus;

  @Inject
  Model(World world, MapState mapState, @ModelEventBus EventBus eventBus) {
    this.world = world;
    this.mapState = mapState;
    this.eventBus = eventBus;
  }

  public void load(Iterable<Cell> cells, State initialState) {
    world.load(cells);
    mapState.initialize(initialState);
  }

  public void prepForSave() {
    mapState.prepForSave();
  }

  public void reset() {
    world.reset();
    mapState.reset();
  }

  public void select(Cell cell) {
    eventBus.post(new SelectCell(cell));
    mapState.select(cell);
  }
}
