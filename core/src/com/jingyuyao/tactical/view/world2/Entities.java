package com.jingyuyao.tactical.view.world2;

import com.badlogic.ashley.core.PooledEngine;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Entities {

  private final PooledEngine engine;
  private final CharacterEntities characterEntities;

  @Inject
  Entities(
      PooledEngine engine,
      CharacterEntities characterEntities) {
    this.engine = engine;
    this.characterEntities = characterEntities;
  }

  void update(float delta) {
    engine.update(delta);
  }

  void reset() {
    characterEntities.reset();
    engine.removeAllEntities();
  }
}
