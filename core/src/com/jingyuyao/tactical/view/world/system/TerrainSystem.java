package com.jingyuyao.tactical.view.world.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.ModelBusListener;
import com.jingyuyao.tactical.model.event.WorldLoaded;
import com.jingyuyao.tactical.model.resource.IntKey;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.model.world.Terrain;
import com.jingyuyao.tactical.view.world.component.Frame;
import com.jingyuyao.tactical.view.world.component.Position;
import com.jingyuyao.tactical.view.world.resource.TileSets;
import com.jingyuyao.tactical.view.world.resource.WorldTexture;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@ModelBusListener
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
      for (int i = 0; i < terrain.getTextures().size(); i++) {
        WorldTexture texture = tileSets.get(terrain.getTextures().get(i));
        frame.setOverlay(i, texture);
      }

      Entity entity = getEngine().createEntity();
      entity.add(position);
      entity.add(frame);

      getEngine().addEntity(entity);
    }
  }
}
