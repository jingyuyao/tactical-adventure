package com.jingyuyao.tactical.view.world;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.event.WorldReset;
import com.jingyuyao.tactical.view.world.system.AnimationSystem;
import com.jingyuyao.tactical.view.world.system.CharacterSystem;
import com.jingyuyao.tactical.view.world.system.EffectsSystem;
import com.jingyuyao.tactical.view.world.system.MarkerSystem;
import com.jingyuyao.tactical.view.world.system.MovingSystem;
import com.jingyuyao.tactical.view.world.system.PlayerSystem;
import com.jingyuyao.tactical.view.world.system.RemoveSystem;
import com.jingyuyao.tactical.view.world.system.RenderSystem;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class WorldEngine {

  private final Engine engine;

  @Inject
  WorldEngine(
      Engine engine,
      AnimationSystem animationSystem,
      CharacterSystem characterSystem,
      EffectsSystem effectsSystem,
      MarkerSystem markerSystem,
      MovingSystem movingSystem,
      PlayerSystem playerSystem,
      RemoveSystem removeSystem,
      RenderSystem renderSystem) {
    this.engine = engine;
    engine.addSystem(animationSystem);
    engine.addSystem(characterSystem);
    engine.addSystem(effectsSystem);
    engine.addSystem(markerSystem);
    engine.addSystem(movingSystem);
    engine.addSystem(playerSystem);
    engine.addSystem(removeSystem);
    engine.addSystem(renderSystem);
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
