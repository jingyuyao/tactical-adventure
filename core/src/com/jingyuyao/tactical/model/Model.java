package com.jingyuyao.tactical.model;

import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.event.SelectCell;
import com.jingyuyao.tactical.model.script.Script;
import com.jingyuyao.tactical.model.state.WorldState;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.model.world.World;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Model {

  private final World world;
  private final WorldState worldState;
  private final ModelBus modelBus;

  @Inject
  Model(World world, WorldState worldState, ModelBus modelBus) {
    this.world = world;
    this.worldState = worldState;
    this.modelBus = modelBus;
  }

  public void initialize(
      Map<Coordinate, Terrain> terrainMap,
      Map<Coordinate, Character> characterMap,
      int turn,
      Script script) {
    world.initialize(terrainMap, characterMap);
    worldState.initialize(turn, script);
  }

  public void prepForSave() {
    worldState.prepForSave();
  }

  public void reset() {
    world.reset();
    worldState.reset();
  }

  public void select(Cell cell) {
    modelBus.post(new SelectCell(cell));
    worldState.select(cell);
  }
}
