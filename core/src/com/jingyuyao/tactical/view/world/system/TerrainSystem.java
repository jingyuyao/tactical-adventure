package com.jingyuyao.tactical.view.world.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.event.WorldLoaded;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.model.world.Terrain;
import com.jingyuyao.tactical.view.world.component.Frame;
import com.jingyuyao.tactical.view.world.component.Position;
import com.jingyuyao.tactical.view.world.resource.TileSets;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class TerrainSystem extends EntitySystem {

  private final TileSets tileSets;

  @Inject
  TerrainSystem(TileSets tileSets) {
    super(SystemPriority.TERRAIN);
    this.tileSets = tileSets;
  }

  @Subscribe
  void worldLoaded(WorldLoaded worldLoaded) {
    for (Cell cell : worldLoaded.getWorld().getWorldCells()) {
      Coordinate coordinate = cell.getCoordinate();
      Terrain terrain = cell.getTerrain();

      Position position = getEngine().createComponent(Position.class);
      position.set(coordinate, WorldZIndex.TERRAIN);

      Frame frame = getEngine().createComponent(Frame.class);
      frame.setTexture(tileSets.get(terrain.getTexture()));

      Entity entity = getEngine().createEntity();
      entity.add(position);
      entity.add(frame);

      getEngine().addEntity(entity);
    }
  }
}
