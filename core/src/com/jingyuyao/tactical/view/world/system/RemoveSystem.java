package com.jingyuyao.tactical.view.world.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.event.WorldReset;
import com.jingyuyao.tactical.view.world.component.Remove;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * System for removing {@link Entity}. This system provides a deterministic and centralized way to
 * know when an entity is removed from the system. This should be the only place we call {@link
 * com.badlogic.ashley.core.Engine#removeEntity(Entity)}.
 */
@Singleton
class RemoveSystem extends IteratingSystem {

  @Inject
  RemoveSystem() {
    super(Family.all(Remove.class).get(), SystemPriority.REMOVE);
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    getEngine().removeEntity(entity);
  }

  @Subscribe
  void worldReset(WorldReset worldReset) {
    getEngine().removeAllEntities();
  }
}
