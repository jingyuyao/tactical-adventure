package com.jingyuyao.tactical.view.world.system;

import static com.google.common.truth.Truth.assertThat;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.jingyuyao.tactical.model.event.WorldReset;
import com.jingyuyao.tactical.view.world.component.Remove;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RemoveSystemTest {

  @Mock
  private WorldReset worldReset;

  private Engine engine;
  private RemoveSystem removeSystem;

  @Before
  public void setUp() {
    engine = new PooledEngine();
    removeSystem = new RemoveSystem();
    assertThat(removeSystem.priority).isEqualTo(SystemPriority.REMOVE);
    engine.addSystem(removeSystem);
  }

  @Test
  public void update() {
    Entity entity = engine.createEntity();
    engine.addEntity(entity);

    engine.update(1f);

    assertThat(engine.getEntities()).containsExactly(entity);

    entity.add(engine.createComponent(Remove.class));

    engine.update(1f);

    assertThat(engine.getEntities()).isEmpty();
  }

  @Test
  public void world_reset() {
    Entity entity = engine.createEntity();
    engine.addEntity(entity);

    removeSystem.worldReset(worldReset);

    assertThat(engine.getEntities()).isEmpty();
  }
}