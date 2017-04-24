package com.jingyuyao.tactical.view.world;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.view.world.system.SystemModule.EntitySystems;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class WorldEngine {

  private final Engine engine;

  @Inject
  WorldEngine(Engine engine, @EntitySystems List<EntitySystem> entitySystems) {
    this.engine = engine;
    for (EntitySystem system : entitySystems) {
      engine.addSystem(system);
    }
  }

  void register(ModelBus modelBus) {
    for (EntitySystem system : engine.getSystems()) {
      modelBus.register(system);
    }
  }

  void update(float delta) {
    engine.update(delta);
  }
}
