package com.jingyuyao.tactical.model;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.BiMap;
import com.google.common.collect.FluentIterable;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.event.SelectCell;
import com.jingyuyao.tactical.model.event.WorldLoad;
import com.jingyuyao.tactical.model.event.WorldReset;
import com.jingyuyao.tactical.model.map.Cell;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.map.Directions;
import com.jingyuyao.tactical.model.state.MapState;
import com.jingyuyao.tactical.model.state.State;
import com.jingyuyao.tactical.model.terrain.Terrain;
import java.util.Map;
import java.util.Map.Entry;

public class World {

  private final EventBus worldEventBus;
  private final MapState mapState;
  private final BiMap<Coordinate, Cell> cellBiMap;
  private int maxHeight;
  private int maxWidth;

  public World(EventBus worldEventBus, MapState mapState, BiMap<Coordinate, Cell> cellBiMap) {
    this.worldEventBus = worldEventBus;
    this.mapState = mapState;
    this.cellBiMap = cellBiMap;
  }

  public int getMaxHeight() {
    return maxHeight;
  }

  public int getMaxWidth() {
    return maxWidth;
  }

  public Coordinate getCoordinate(Cell cell) {
    return cellBiMap.inverse().get(cell);
  }

  public FluentIterable<Character> getCharacters() {
    return FluentIterable.from(cellBiMap.values())
        .filter(new Predicate<Cell>() {
          @Override
          public boolean apply(Cell input) {
            return input.hasCharacter();
          }
        })
        .transform(new Function<Cell, Character>() {
          @Override
          public Character apply(Cell input) {
            return input.getCharacter();
          }
        });
  }

  public FluentIterable<Terrain> getTerrains() {
    return FluentIterable.from(cellBiMap.values())
        .filter(new Predicate<Cell>() {
          @Override
          public boolean apply(Cell input) {
            return input.hasTerrain();
          }
        })
        .transform(new Function<Cell, Terrain>() {
          @Override
          public Terrain apply(Cell input) {
            return input.getTerrain();
          }
        });
  }

  public Iterable<Cell> getNeighbors(final Coordinate from) {
    return FluentIterable
        .from(Directions.ALL)
        .transform(new Function<Coordinate, Coordinate>() {
          @Override
          public Coordinate apply(Coordinate input) {
            return from.offsetBy(input);
          }
        })
        .filter(new Predicate<Coordinate>() {
          @Override
          public boolean apply(Coordinate input) {
            return cellBiMap.containsKey(input);
          }
        })
        .transform(new Function<Coordinate, Cell>() {
          @Override
          public Cell apply(Coordinate input) {
            return cellBiMap.get(input);
          }
        });
  }

  public void load(State initialState, Map<Coordinate, Cell> cellMap) {
    for (Entry<Coordinate, Cell> entry : cellMap.entrySet()) {
      cellBiMap.put(entry.getKey(), entry.getValue());
      maxWidth = Math.max(maxWidth, entry.getKey().getX() + 1);
      maxHeight = Math.max(maxHeight, entry.getKey().getY() + 1);
    }
    mapState.initialize(initialState);
    worldEventBus.post(new WorldLoad(cellMap));
  }

  public void reset() {
    cellBiMap.clear();
    worldEventBus.post(new WorldReset());
  }

  public void select(Cell cell) {
    Coordinate cellCoordinate = getCoordinate(cell);
    worldEventBus.post(new SelectCell(cellCoordinate, cell));
    mapState.select(cellCoordinate, cell);
  }

  public void moveCharacter(Cell from, Cell to) {
    Preconditions.checkArgument(from.hasCharacter());
    Preconditions.checkArgument(!to.hasCharacter());
    to.setCharacter(from.getCharacter());
    from.setCharacter(null);
  }
}
