package com.jingyuyao.tactical.model.world;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.Allegiance;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.event.WorldLoad;
import com.jingyuyao.tactical.model.event.WorldReset;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.model.world.WorldModule.BackingCellMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class World {

  private final ModelBus modelBus;
  private final CellFactory cellFactory;
  private final Map<Coordinate, Cell> cellMap;
  private int maxHeight;
  private int maxWidth;

  @Inject
  World(ModelBus modelBus, CellFactory cellFactory, @BackingCellMap Map<Coordinate, Cell> cellMap) {
    this.modelBus = modelBus;
    this.cellFactory = cellFactory;
    this.cellMap = cellMap;
  }

  public void initialize(
      Map<Coordinate, Terrain> terrainMap,
      Map<Coordinate, Ship> shipMap) {
    for (Entry<Coordinate, Terrain> entry : terrainMap.entrySet()) {
      Coordinate coordinate = entry.getKey();
      if (cellMap.containsKey(coordinate)) {
        throw new IllegalArgumentException("Duplicated terrain detected");
      }
      Cell cell = cellFactory.create(coordinate, entry.getValue());
      cellMap.put(coordinate, cell);
      // index is zero based
      maxWidth = Math.max(maxWidth, coordinate.getX() + 1);
      maxHeight = Math.max(maxHeight, coordinate.getY() + 1);
    }
    for (Entry<Coordinate, Ship> entry : shipMap.entrySet()) {
      Coordinate coordinate = entry.getKey();
      if (!cellMap.containsKey(coordinate)) {
        throw new IllegalArgumentException("Ship not on a terrain");
      }
      Cell cell = cellMap.get(coordinate);
      if (cell.ship().isPresent()) {
        throw new IllegalArgumentException("Ship occupying same space as another");
      }
      cell.spawnShip(entry.getValue());
    }
    modelBus.post(new WorldLoad(cellMap.values()));
  }

  public void reset() {
    cellMap.clear();
    maxWidth = 0;
    maxHeight = 0;
    modelBus.post(new WorldReset());
  }

  public int getMaxHeight() {
    return maxHeight;
  }

  public int getMaxWidth() {
    return maxWidth;
  }

  public Optional<Cell> cell(int x, int y) {
    return cell(new Coordinate(x, y));
  }

  public Optional<Cell> cell(Coordinate coordinate) {
    return Optional.fromNullable(cellMap.get(coordinate));
  }

  public ImmutableList<Cell> getShipSnapshot() {
    return FluentIterable.from(cellMap.values())
        .filter(new Predicate<Cell>() {
          @Override
          public boolean apply(Cell input) {
            return input.ship().isPresent();
          }
        }).toList();
  }

  public void resetPlayerShipStats() {
    for (Cell cell : cellMap.values()) {
      for (Ship ship : cell.ship().asSet()) {
        if (ship.getAllegiance().equals(Allegiance.PLAYER)) {
          ship.fullHeal();
          ship.setControllable(true);
        }
      }
    }
  }
}
