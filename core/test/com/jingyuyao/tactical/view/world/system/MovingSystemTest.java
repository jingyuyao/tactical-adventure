package com.jingyuyao.tactical.view.world.system;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.event.MyFuture;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.view.world.WorldConfig;
import com.jingyuyao.tactical.view.world.component.Moving;
import com.jingyuyao.tactical.view.world.component.Position;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MovingSystemTest {

  private static final Coordinate C1 = new Coordinate(11, 11);
  private static final Coordinate C2 = new Coordinate(12, 12);

  @Mock
  private WorldConfig worldConfig;

  private Engine engine;

  @Before
  public void setUp() {
    engine = new PooledEngine();
    MovingSystem movingSystem = new MovingSystem(
        worldConfig,
        ComponentMapper.getFor(Position.class),
        ComponentMapper.getFor(Moving.class));
    assertThat(movingSystem.priority).isEqualTo(SystemPriority.MOVING);
    engine.addSystem(movingSystem);
  }

  @Test
  public void processEntity() {
    Position position = engine.createComponent(Position.class);
    position.setX(10f);
    position.setY(10f);
    position.setZ(WorldZIndex.CHARACTER);
    Moving moving = engine.createComponent(Moving.class);
    moving.setFuture(new MyFuture());
    moving.setPath(ImmutableList.of(C1, C2));
    when(worldConfig.getCharacterMoveUnitPerSec()).thenReturn(1f);
    Entity entity = engine.createEntity();
    entity.add(position);
    entity.add(moving);
    engine.addEntity(entity);

    engine.update(0.5f);

    assertThat(position.getX()).isEqualTo(10.5f);
    assertThat(position.getY()).isEqualTo(10.5f);
    assertThat(entity.getComponent(Position.class)).isSameAs(position);
    assertThat(entity.getComponent(Moving.class)).isSameAs(moving);

    engine.update(0.5f);

    assertThat(position.getX()).isEqualTo(11f);
    assertThat(position.getY()).isEqualTo(11f);
    assertThat(entity.getComponent(Position.class)).isSameAs(position);
    assertThat(entity.getComponent(Moving.class)).isSameAs(moving);

    engine.update(1.5f);

    assertThat(position.getX()).isEqualTo(12f);
    assertThat(position.getY()).isEqualTo(12f);
    assertThat(entity.getComponent(Position.class)).isSameAs(position);
    assertThat(entity.getComponent(Moving.class)).isNull();
  }
}