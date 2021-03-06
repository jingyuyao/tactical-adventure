package com.jingyuyao.tactical.view.world.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Public interface to all the {@link EntitySystem}.
 */
@Singleton
public class Systems {

  private final List<EntitySystem> systems;

  @Inject
  Systems(
      AnimationSystem animationSystem,
      ShipSystem shipSystem,
      EffectsSystem effectsSystem,
      MarkerSystem markerSystem,
      MovingSystem movingSystem,
      RemoveSystem removeSystem,
      RenderSystem renderSystem,
      TerrainSystem terrainSystem) {
    this.systems = Arrays.asList(
        animationSystem,
        shipSystem,
        effectsSystem,
        markerSystem,
        movingSystem,
        removeSystem,
        renderSystem,
        terrainSystem
    );
  }

  public void addTo(Engine engine) {
    for (EntitySystem system : systems) {
      engine.addSystem(system);
    }
  }
}
