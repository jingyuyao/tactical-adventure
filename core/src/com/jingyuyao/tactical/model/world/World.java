package com.jingyuyao.tactical.model.world;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.ModelModule.ModelEventBus;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
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

  private final EventBus eventBus;
  private final CellFactory cellFactory;
  private final Map<Coordinate, Cell> cellMap;
  private int maxHeight;
  private int maxWidth;

  @Inject
  World(
      @ModelEventBus EventBus eventBus,
      CellFactory cellFactory,
      @BackingCellMap Map<Coordinate, Cell> cellMap) {
    this.eventBus = eventBus;
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
    eventBus.post(new WorldLoad(cellMap.values()));
  }

  public void reset() {
    cellMap.clear();
    eventBus.post(new WorldReset());
  }

  public int getMaxHeight() {
    return maxHeight;
  }

  public int getMaxWidth() {
    return maxWidth;
  }

  public FluentIterable<Cell> getCells() {
    return FluentIterable.from(cellMap.values());
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

  public Map<Coordinate, Player> getPlayers() {
    return Maps.transformValues(Maps.filterValues(cellMap, new Predicate<Cell>() {
      @Override
      public boolean apply(Cell input) {
        return input.hasPlayer();
      }
    }), new Function<Cell, Player>() {
      @Override
      public Player apply(Cell input) {
        return input.getPlayer();
      }
    });
  }

  public Map<Coordinate, Enemy> getEnemies() {
    return Maps.transformValues(Maps.filterValues(cellMap, new Predicate<Cell>() {
      @Override
      public boolean apply(Cell input) {
        return input.hasEnemy();
      }
    }), new Function<Cell, Enemy>() {
      @Override
      public Enemy apply(Cell input) {
        return input.getEnemy();
      }
    });
  }
}
