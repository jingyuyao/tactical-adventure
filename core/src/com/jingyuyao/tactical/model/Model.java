package com.jingyuyao.tactical.model;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.ModelModule.ModelEventBus;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.event.SelectCell;
import com.jingyuyao.tactical.model.state.Waiting;
import com.jingyuyao.tactical.model.state.WorldState;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.model.world.World;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class Model {

  private final World world;
  private final WorldState worldState;
  private final EventBus eventBus;
  private final Provider<Waiting> waitingProvider;

  @Inject
  Model(
      World world,
      WorldState worldState,
      @ModelEventBus EventBus eventBus,
      Provider<Waiting> waitingProvider) {
    this.world = world;
    this.worldState = worldState;
    this.eventBus = eventBus;
    this.waitingProvider = waitingProvider;
  }

  public void initialize(
      Map<Coordinate, Terrain> terrainMap,
      Map<Coordinate, Character> characterMap) {
    world.initialize(terrainMap, characterMap);
    worldState.initialize(waitingProvider.get());
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
