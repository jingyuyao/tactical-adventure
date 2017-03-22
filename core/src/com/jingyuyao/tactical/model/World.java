package com.jingyuyao.tactical.model;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
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

public class World {

  private final EventBus worldEventBus;
  private final MapState mapState;
  private final Map<Coordinate, Cell> cellMap;
  private int maxHeight;
  private int maxWidth;

  public World(EventBus worldEventBus, MapState mapState, Map<Coordinate, Cell> cellMap) {
    this.worldEventBus = worldEventBus;
    this.mapState = mapState;
    this.cellMap = cellMap;
  }

  public int getMaxHeight() {
    return maxHeight;
  }

  public int getMaxWidth() {
    return maxWidth;
  }

  public FluentIterable<Character> getCharacters() {
    return FluentIterable.from(cellMap.values())
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
    return FluentIterable.from(cellMap.values())
        .transform(new Function<Cell, Terrain>() {
          @Override
          public Terrain apply(Cell input) {
            return input.getTerrain();
          }
        });
  }

  public Iterable<Cell> getNeighbors(final Cell from) {
    return FluentIterable
        .from(Directions.ALL)
        .transform(new Function<Coordinate, Coordinate>() {
          @Override
          public Coordinate apply(Coordinate input) {
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

  public void load(State initialState, Iterable<Cell> cells) {
    for (Cell cell : cells) {
      Coordinate coordinate = cell.getCoordinate();
      cellMap.put(coordinate, cell);
      // index is zero based
      maxWidth = Math.max(maxWidth, coordinate.getX() + 1);
      maxHeight = Math.max(maxHeight, coordinate.getY() + 1);
    }
    mapState.initialize(initialState);
    worldEventBus.post(new WorldLoad(cells));
  }

  public void reset() {
    cellMap.clear();
    worldEventBus.post(new WorldReset());
  }

  public void select(Cell cell) {
    worldEventBus.post(new SelectCell(cell));
    mapState.select(cell);
  }
}
