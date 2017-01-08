package com.jingyuyao.tactical.model.map;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.Coordinate;
import com.jingyuyao.tactical.model.common.EventBusObject;
import com.jingyuyao.tactical.model.common.Grid;
import com.jingyuyao.tactical.model.common.ManagedBy;
import com.jingyuyao.tactical.model.event.ClearMap;
import com.jingyuyao.tactical.model.event.NewMap;
import java.util.Iterator;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * A {@link Terrain} grid backed by a {@link Grid}.
 */
// TODO: sync changes terrains' coordinates with grid when terrain can change locations
@Singleton
public class Terrains extends EventBusObject
    implements ManagedBy<NewMap, ClearMap>, Iterable<Terrain> {

  private Grid<Terrain> grid;

  @Inject
  public Terrains(EventBus eventBus) {
    super(eventBus);
    register();
  }

  @Subscribe
  @Override
  public void initialize(NewMap data) {
    this.grid = data.getTerrainGrid();
  }

  @Subscribe
  @Override
  public void dispose(ClearMap clearMap) {
    grid = null;
  }

  public int getWidth() {
    return grid.getWidth();
  }

  public int getHeight() {
    return grid.getHeight();
  }

  public Terrain get(Coordinate coordinate) {
    return grid.get(coordinate);
  }

  public Iterable<Terrain> getAll(Iterable<Coordinate> coordinates) {
    return grid.getAll(coordinates);
  }

  @Override
  public Iterator<Terrain> iterator() {
    return grid.iterator();
  }
}
