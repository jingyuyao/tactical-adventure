package com.jingyuyao.tactical.view.world;

import com.badlogic.ashley.core.Engine;
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
class ViewEngine {

  private final Engine engine;
  private final CharacterSystem characterSystem;

  @Inject
  ViewEngine(
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
    this.characterSystem = characterSystem;
  }

  void update(float delta) {
    engine.update(delta);
  }

  void reset() {
    characterSystem.reset();
    engine.removeAllEntities();
  }
}
