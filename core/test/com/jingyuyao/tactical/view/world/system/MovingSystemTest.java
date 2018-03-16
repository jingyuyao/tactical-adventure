package com.jingyuyao.tactical.view.world.system;

import static com.google.common.truth.Truth.assertThat;
import static com.jingyuyao.tactical.model.world.CoordinateTest.C0_0;
import static com.jingyuyao.tactical.model.world.CoordinateTest.C0_1;
import static com.jingyuyao.tactical.model.world.CoordinateTest.C1_0;
import static com.jingyuyao.tactical.model.world.CoordinateTest.C1_1;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.jingyuyao.tactical.model.event.Promise;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.ship.ShipGroup;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.model.world.Direction;
import com.jingyuyao.tactical.view.world.WorldCamera;
import com.jingyuyao.tactical.view.world.WorldConfig;
import com.jingyuyao.tactical.view.world.component.Frame;
import com.jingyuyao.tactical.view.world.component.Moving;
import com.jingyuyao.tactical.view.world.component.Position;
import com.jingyuyao.tactical.view.world.component.ShipComponent;
import java.util.Arrays;
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
  @Mock
  private WorldCamera worldCamera;
  @Mock
  private Ship ship;

  private Engine engine;

  @Before
  public void setUp() {
    engine = new PooledEngine();
    MovingSystem movingSystem = new MovingSystem(
        worldConfig,
        worldCamera,
        ComponentMapper.getFor(Position.class),
        ComponentMapper.getFor(Moving.class),
        ComponentMapper.getFor(ShipComponent.class),
        ComponentMapper.getFor(Frame.class));
    assertThat(movingSystem.priority).isEqualTo(SystemPriority.MOVING);
    engine.addSystem(movingSystem);
  }

  @Test
  public void processEntity() {
    Position position = engine.createComponent(Position.class);
    position.setX(10f);
    position.setY(10f);
    position.setZ(WorldZIndex.SHIP);
    Moving moving = engine.createComponent(Moving.class);
    moving.setPromise(new Promise());
    moving.setPath(Arrays.asList(C1, C2));
    when(worldConfig.getShipMoveUnitPerSec()).thenReturn(1f);
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

  @Test
  public void processNotPlayerShip() {
    Position position = engine.createComponent(Position.class);
    position.setX(10f);
    position.setY(10f);
    position.setZ(WorldZIndex.SHIP);
    Moving moving = engine.createComponent(Moving.class);
    moving.setPromise(new Promise());
    moving.setPath(Arrays.asList(C1, C2));
    when(worldConfig.getShipMoveUnitPerSec()).thenReturn(1f);
    ShipComponent shipComponent = engine.createComponent(ShipComponent.class);
    shipComponent.setShip(ship);
    when(ship.inGroup(ShipGroup.PLAYER)).thenReturn(false);
    Entity entity = engine.createEntity();
    entity.add(position);
    entity.add(moving);
    entity.add(shipComponent);
    engine.addEntity(entity);

    engine.update(0.5f);

    assertThat(position.getX()).isEqualTo(10.5f);
    assertThat(position.getY()).isEqualTo(10.5f);
    assertThat(entity.getComponent(Position.class)).isSameAs(position);
    assertThat(entity.getComponent(Moving.class)).isSameAs(moving);
    verify(worldCamera).setCameraPosition(10.5f, 10.5f);

    engine.update(0.5f);

    assertThat(position.getX()).isEqualTo(11f);
    assertThat(position.getY()).isEqualTo(11f);
    assertThat(entity.getComponent(Position.class)).isSameAs(position);
    assertThat(entity.getComponent(Moving.class)).isSameAs(moving);
    verify(worldCamera).setCameraPosition(11f, 11f);

    engine.update(1.5f);

    assertThat(position.getX()).isEqualTo(12f);
    assertThat(position.getY()).isEqualTo(12f);
    assertThat(entity.getComponent(Position.class)).isSameAs(position);
    assertThat(entity.getComponent(Moving.class)).isNull();
    verify(worldCamera).setCameraPosition(12f, 12f);
  }

  @Test
  public void processPlayerShip() {
    Position position = engine.createComponent(Position.class);
    position.setX(10f);
    position.setY(10f);
    position.setZ(WorldZIndex.SHIP);
    Moving moving = engine.createComponent(Moving.class);
    moving.setPromise(new Promise());
    moving.setPath(Arrays.asList(C1, C2));
    when(worldConfig.getShipMoveUnitPerSec()).thenReturn(1f);
    ShipComponent shipComponent = engine.createComponent(ShipComponent.class);
    shipComponent.setShip(ship);
    when(ship.inGroup(ShipGroup.PLAYER)).thenReturn(true);
    Entity entity = engine.createEntity();
    entity.add(position);
    entity.add(moving);
    entity.add(shipComponent);
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

    verifyZeroInteractions(worldCamera);
  }

  @Test
  public void processDirection() {
    Position position = engine.createComponent(Position.class);
    position.setX(C0_0.getX());
    position.setY(C0_0.getY());
    position.setZ(WorldZIndex.SHIP);
    Moving moving = engine.createComponent(Moving.class);
    moving.setPromise(new Promise());
    moving.setPath(Arrays.asList(C0_1, C1_1, C1_0, C0_0));
    when(worldConfig.getShipMoveUnitPerSec()).thenReturn(1f);
    ShipComponent shipComponent = engine.createComponent(ShipComponent.class);
    shipComponent.setShip(ship);
    when(ship.inGroup(ShipGroup.PLAYER)).thenReturn(true);
    Frame frame = engine.createComponent(Frame.class);
    Entity entity = engine.createEntity();
    entity.add(position);
    entity.add(moving);
    entity.add(shipComponent);
    entity.add(frame);
    engine.addEntity(entity);

    engine.update(0.5f);

    assertThat(frame.direction()).hasValue(Direction.UP);

    engine.update(0.5f);

    assertThat(frame.direction()).hasValue(Direction.UP);

    engine.update(0.5f);

    assertThat(frame.direction()).hasValue(Direction.RIGHT);

    engine.update(0.5f);

    assertThat(frame.direction()).hasValue(Direction.RIGHT);

    engine.update(0.5f);

    assertThat(frame.direction()).hasValue(Direction.DOWN);

    engine.update(0.5f);

    assertThat(frame.direction()).hasValue(Direction.DOWN);

    engine.update(0.5f);

    assertThat(frame.direction()).hasValue(Direction.LEFT);

    engine.update(0.5f);

    assertThat(frame.direction()).hasValue(Direction.LEFT);
  }
}
