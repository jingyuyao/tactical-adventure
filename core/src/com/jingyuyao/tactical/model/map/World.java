package com.jingyuyao.tactical.model.map;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.ModelModule.ModelEventBus;
import com.jingyuyao.tactical.model.event.WorldLoad;
import com.jingyuyao.tactical.model.event.WorldReset;
import com.jingyuyao.tactical.model.map.MapModule.BackingCellMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class World {

  private final EventBus eventBus;
  private final Map<Coordinate, Cell> cellMap;
  private int maxHeight;
  private int maxWidth;

  @Inject
  World(@ModelEventBus EventBus eventBus, @BackingCellMap Map<Coordinate, Cell> cellMap) {
    this.eventBus = eventBus;
    this.cellMap = cellMap;
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

  public void load(Iterable<Cell> cells) {
    for (Cell cell : cells) {
      Coordinate coordinate = cell.getCoordinate();
      cellMap.put(coordinate, cell);
      // index is zero based
      maxWidth = Math.max(maxWidth, coordinate.getX() + 1);
      maxHeight = Math.max(maxHeight, coordinate.getY() + 1);
    }
    eventBus.post(new WorldLoad(cells));
  }

  public void reset() {
    cellMap.clear();
    eventBus.post(new WorldReset());
  }
}
