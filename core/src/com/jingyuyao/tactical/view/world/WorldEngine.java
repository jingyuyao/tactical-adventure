package com.jingyuyao.tactical.view.world;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.event.WorldReset;
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

  void register(EventBus eventBus) {
    eventBus.register(this);
    for (EntitySystem system : engine.getSystems()) {
      eventBus.register(system);
    }
  }

  void update(float delta) {
    engine.update(delta);
  }

  @Subscribe
  void worldReset(WorldReset worldReset) {
    engine.removeAllEntities();
  }
}
