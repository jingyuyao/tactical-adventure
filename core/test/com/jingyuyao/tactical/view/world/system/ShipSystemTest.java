package com.jingyuyao.tactical.view.world.system;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.jingyuyao.tactical.model.event.ActivatedShip;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.event.InstantMoveShip;
import com.jingyuyao.tactical.model.event.MoveShip;
import com.jingyuyao.tactical.model.event.Promise;
import com.jingyuyao.tactical.model.event.RemoveShip;
import com.jingyuyao.tactical.model.event.SpawnShip;
import com.jingyuyao.tactical.model.event.WorldLoaded;
import com.jingyuyao.tactical.model.resource.StringKey;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.ship.ShipGroup;
import com.jingyuyao.tactical.model.state.ControllingState;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.model.world.Path;
import com.jingyuyao.tactical.model.world.World;
import com.jingyuyao.tactical.view.world.component.Frame;
import com.jingyuyao.tactical.view.world.component.LoopAnimation;
import com.jingyuyao.tactical.view.world.component.Moving;
import com.jingyuyao.tactical.view.world.component.Position;
import com.jingyuyao.tactical.view.world.component.Remove;
import com.jingyuyao.tactical.view.world.component.ShipComponent;
import com.jingyuyao.tactical.view.world.resource.Animations;
import com.jingyuyao.tactical.view.world.resource.Colors;
import com.jingyuyao.tactical.view.world.resource.Markers;
import com.jingyuyao.tactical.view.world.resource.WorldTexture;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ShipSystemTest {

  private static final Coordinate C1 = new Coordinate(5, 5);
  private static final Coordinate C2 = new Coordinate(6, 6);
  private static final Coordinate T1 = new Coordinate(6, 7);
  private static final Coordinate T2 = new Coordinate(7, 7);
  private static final Coordinate T3 = new Coordinate(8, 7);
  private static final Coordinate T4 = new Coordinate(8, 8);

  @Mock
  private Markers markers;
  @Mock
  private Animations animations;
  @Mock
  private Cell cell;
  @Mock
  private Cell cell2;
  @Mock
  private Cell cell3;
  @Mock
  private Cell cell4;
  @Mock
  private Cell cell5;
  @Mock
  private Cell cell6;
  @Mock
  private Ship ship;
  @Mock
  private StringKey stringKey;
  @Mock
  private WorldTexture texture;
  @Mock
  private WorldLoaded worldLoaded;
  @Mock
  private SpawnShip spawnShip;
  @Mock
  private RemoveShip removeShip;
  @Mock
  private InstantMoveShip instantMoveShip;
  @Mock
  private MoveShip moveShip;
  @Mock
  private ControllingState controllingState;
  @Mock
  private ActivatedShip activatedShip;
  @Mock
  private ExitState exitState;
  @Mock
  private World world;
  @Mock
  private Path path;
  @Mock
  private Promise promise;

  private Engine engine;
  private ShipSystem shipSystem;

  @Before
  public void setUp() {
    engine = new PooledEngine();
    shipSystem =
        new ShipSystem(
            markers,
            animations,
            ComponentMapper.getFor(ShipComponent.class),
            ComponentMapper.getFor(Frame.class));
    assertThat(shipSystem.priority).isEqualTo(SystemPriority.SHIP);
    engine.addSystem(shipSystem);
  }

  @Test
  public void process_entities_enemy() {
    when(ship.inGroup(ShipGroup.ENEMY)).thenReturn(true);
    ShipComponent shipComponent = new ShipComponent();
    shipComponent.setShip(ship);
    Frame frame = new Frame();
    Entity entity = new Entity();
    entity.add(shipComponent);
    entity.add(frame);

    shipSystem.processEntity(entity, 0f);

    assertThat(frame.getColor()).isEqualTo(Colors.RED_500);
  }

  @Test
  public void process_entities_player_controllable() {
    when(ship.inGroup(ShipGroup.PLAYER)).thenReturn(true);
    when(ship.isControllable()).thenReturn(true);
    ShipComponent shipComponent = new ShipComponent();
    shipComponent.setShip(ship);
    Frame frame = new Frame();
    Entity entity = new Entity();
    entity.add(shipComponent);
    entity.add(frame);

    shipSystem.processEntity(entity, 0f);

    assertThat(frame.getColor()).isEqualTo(Colors.BLUE_300);
  }

  @Test
  public void process_entities_player_uncontrollable() {
    when(ship.inGroup(ShipGroup.PLAYER)).thenReturn(true);
    when(ship.isControllable()).thenReturn(false);
    ShipComponent shipComponent = new ShipComponent();
    shipComponent.setShip(ship);
    Frame frame = new Frame();
    Entity entity = new Entity();
    entity.add(shipComponent);
    entity.add(frame);

    shipSystem.processEntity(entity, 0f);

    assertThat(frame.getColor()).isEqualTo(Colors.GREY_500);
  }

  @Test
  public void world_loaded() {
    LoopAnimation animation = new LoopAnimation(10, new WorldTexture[]{texture});
    when(worldLoaded.getWorld()).thenReturn(world);
    when(world.getActiveShips()).thenReturn(ImmutableMap.of(cell, ship));
    when(cell.getCoordinate()).thenReturn(C1);
    when(ship.getAnimation()).thenReturn(stringKey);
    when(animations.getLoop(stringKey)).thenReturn(animation);

    shipSystem.worldLoaded(worldLoaded);

    ImmutableArray<Entity> entities = engine.getEntities();
    assertThat(entities).hasSize(1);
    Entity entity = entities.first();
    Position position = entity.getComponent(Position.class);
    assertThat(position).isNotNull();
    assertThat(position.getX()).isEqualTo((float) C1.getX());
    assertThat(position.getY()).isEqualTo((float) C1.getY());
    assertThat(position.getZ()).isEqualTo(WorldZIndex.SHIP);
    Frame frame = entity.getComponent(Frame.class);
    assertThat(frame).isNotNull();
    LoopAnimation loopAnimation = entity.getComponent(LoopAnimation.class);
    assertThat(loopAnimation).isEqualTo(animation);
    ShipComponent shipComponent = entity.getComponent(ShipComponent.class);
    assertThat(shipComponent).isNotNull();
    assertThat(shipComponent.getShip()).isEqualTo(ship);
  }

  @Test
  public void spawn_ship() {
    LoopAnimation animation = new LoopAnimation(10, new WorldTexture[]{texture});
    when(spawnShip.getObject()).thenReturn(cell);
    when(cell.getCoordinate()).thenReturn(C1);
    when(cell.ship()).thenReturn(Optional.of(ship));
    when(ship.getAnimation()).thenReturn(stringKey);
    when(animations.getLoop(stringKey)).thenReturn(animation);

    shipSystem.spawnShip(spawnShip);

    ImmutableArray<Entity> entities = engine.getEntities();
    assertThat(entities).hasSize(1);
    Entity entity = entities.first();
    Position position = entity.getComponent(Position.class);
    assertThat(position).isNotNull();
    assertThat(position.getX()).isEqualTo((float) C1.getX());
    assertThat(position.getY()).isEqualTo((float) C1.getY());
    assertThat(position.getZ()).isEqualTo(WorldZIndex.SHIP);
    Frame frame = entity.getComponent(Frame.class);
    assertThat(frame).isNotNull();
    LoopAnimation loopAnimation = entity.getComponent(LoopAnimation.class);
    assertThat(loopAnimation).isEqualTo(animation);
    ShipComponent shipComponent = entity.getComponent(ShipComponent.class);
    assertThat(shipComponent).isNotNull();
    assertThat(shipComponent.getShip()).isEqualTo(ship);
  }

  @Test
  public void remove_ship() {
    spawn_ship();
    when(removeShip.getObject()).thenReturn(ship);

    shipSystem.removeShip(removeShip);

    ImmutableArray<Entity> entities = engine.getEntities();
    assertThat(entities).hasSize(1);
    Entity entity = entities.first();
    assertThat(entity.getComponent(Remove.class)).isNotNull();
  }

  @Test
  public void instant_move() {
    spawn_ship();
    when(instantMoveShip.getShip()).thenReturn(ship);
    when(instantMoveShip.getDestination()).thenReturn(cell2);
    when(cell2.getCoordinate()).thenReturn(C2);

    shipSystem.instantMoveShip(instantMoveShip);

    ImmutableArray<Entity> entities = engine.getEntities();
    assertThat(entities).hasSize(1);
    Entity entity = entities.first();
    Position position = entity.getComponent(Position.class);
    assertThat(position).isNotNull();
    assertThat(position.getX()).isEqualTo((float) C2.getX());
    assertThat(position.getY()).isEqualTo((float) C2.getY());
    assertThat(position.getZ()).isEqualTo(WorldZIndex.SHIP);
  }

  @Test
  public void move() {
    spawn_ship();
    when(moveShip.getShip()).thenReturn(ship);
    when(moveShip.getPromise()).thenReturn(promise);
    when(moveShip.getPath()).thenReturn(path);
    when(path.getTrack()).thenReturn(Arrays.asList(cell3, cell4, cell5, cell6));
    when(cell3.getCoordinate()).thenReturn(T1);
    when(cell4.getCoordinate()).thenReturn(T2);
    when(cell5.getCoordinate()).thenReturn(T3);
    when(cell6.getCoordinate()).thenReturn(T4);

    shipSystem.moveShip(moveShip);

    ImmutableArray<Entity> entities = engine.getEntities();
    assertThat(entities).hasSize(1);
    Entity entity = entities.first();
    Moving moving = entity.getComponent(Moving.class);
    assertThat(moving).isNotNull();
    assertThat(moving.getCurrentIndex()).isEqualTo(0);
    assertThat(moving.getPromise()).isSameAs(promise);
    assertThat(moving.getPath()).containsExactly(T3, T4).inOrder();
  }

  @Test
  public void player_state() {
    spawn_ship();
    when(controllingState.getShip()).thenReturn(ship);
    when(markers.getActivated()).thenReturn(texture);

    shipSystem.playerState(controllingState);

    ImmutableArray<Entity> entities = engine.getEntities();
    assertThat(entities).hasSize(1);
    Entity entity = entities.first();
    Frame frame = entity.getComponent(Frame.class);
    assertThat(frame).isNotNull();
    assertThat(frame.getOverlays()).containsExactly(texture);

    shipSystem.playerState(controllingState);

    assertThat(frame.getOverlays()).containsExactly(texture);
  }

  @Test
  public void activated_enemy() {
    spawn_ship();
    when(activatedShip.getObject()).thenReturn(ship);
    when(markers.getActivated()).thenReturn(texture);

    shipSystem.activatedEnemy(activatedShip);

    ImmutableArray<Entity> entities = engine.getEntities();
    assertThat(entities).hasSize(1);
    Entity entity = entities.first();
    Frame frame = entity.getComponent(Frame.class);
    assertThat(frame).isNotNull();
    assertThat(frame.getOverlays()).containsExactly(texture);

    shipSystem.activatedEnemy(activatedShip);

    assertThat(frame.getOverlays()).containsExactly(texture);
  }

  @Test
  public void exit_state() {
    spawn_ship();
    when(activatedShip.getObject()).thenReturn(ship);
    when(markers.getActivated()).thenReturn(texture);

    shipSystem.activatedEnemy(activatedShip);

    ImmutableArray<Entity> entities = engine.getEntities();
    assertThat(entities).hasSize(1);
    Entity entity = entities.first();
    Frame frame = entity.getComponent(Frame.class);
    assertThat(frame).isNotNull();
    assertThat(frame.getOverlays()).containsExactly(texture);

    shipSystem.exitState(exitState);

    assertThat(frame.getOverlays()).isEmpty();
  }
}