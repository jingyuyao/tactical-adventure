package com.jingyuyao.tactical.model.world;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.event.WorldLoad;
import com.jingyuyao.tactical.model.event.WorldReset;
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
      Map<Coordinate, Character> characterMap) {
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
    for (Entry<Coordinate, Character> entry : characterMap.entrySet()) {
      Coordinate coordinate = entry.getKey();
      if (!cellMap.containsKey(coordinate)) {
        throw new IllegalArgumentException("Character not on a terrain");
      }
      Cell cell = cellMap.get(coordinate);
      if (cell.hasCharacter()) {
        throw new IllegalArgumentException("Character occupying same space as another");
      }
      cell.spawnCharacter(entry.getValue());
    }
    modelBus.post(new WorldLoad(cellMap.values()));
  }

  public void reset() {
    cellMap.clear();
    modelBus.post(new WorldReset());
  }

  public int getMaxHeight() {
    return maxHeight;
  }

  public int getMaxWidth() {
    return maxWidth;
  }

  public Optional<Cell> getCell(int x, int y) {
    return Optional.fromNullable(cellMap.get(new Coordinate(x, y)));
  }

  public ImmutableList<Cell> getCharacterSnapshot() {
    return FluentIterable.from(cellMap.values())
        .filter(new Predicate<Cell>() {
          @Override
          public boolean apply(Cell input) {
            return input.hasCharacter();
          }
        }).toList();
  }

  public Iterable<Cell> getNeighbors(final Cell from) {
    return FluentIterable
        .from(Direction.values())
        .transform(new Function<Direction, Coordinate>() {
          @Override
          public Coordinate apply(Direction input) {
            return from.getCoordinate().offsetBy(input);
          }
        })
        .filter(new Predicate<Coordinate>() {
          @Override
          public boolean apply(Coordinate input) {
            return cellMap.containsKey(input);
          }
        })
        .transform(new Function<Coordinate, Cell>() {
          @Override
          public Cell apply(Coordinate input) {
            return cellMap.get(input);
          }
        });
  }

  public Optional<Cell> getNeighbor(Cell from, Direction direction) {
    return Optional.fromNullable(cellMap.get(from.getCoordinate().offsetBy(direction)));
  }

  public void fullHealPlayers() {
    for (Cell cell : cellMap.values()) {
      if (cell.hasPlayer()) {
        cell.getPlayer().fullHeal();
      }
    }
  }
}
