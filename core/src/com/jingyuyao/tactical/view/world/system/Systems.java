package com.jingyuyao.tactical.view.world.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.google.common.collect.ImmutableList;
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
      PlayerSystem playerSystem,
      RemoveSystem removeSystem,
      RenderSystem renderSystem) {
    this.systems = ImmutableList.of(
        animationSystem,
        shipSystem,
        effectsSystem,
        markerSystem,
        movingSystem,
        playerSystem,
        removeSystem,
        renderSystem
    );
  }

  public void addTo(Engine engine) {
    for (EntitySystem system : systems) {
      engine.addSystem(system);
    }
  }
}
