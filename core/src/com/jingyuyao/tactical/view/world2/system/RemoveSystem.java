package com.jingyuyao.tactical.view.world2.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.jingyuyao.tactical.view.world2.component.Remove;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RemoveSystem extends IteratingSystem {

  @Inject
  RemoveSystem() {
    super(Family.all(Remove.class).get());
    this.priority = SystemPriority.REMOVE;
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    getEngine().removeEntity(entity);
  }
}
