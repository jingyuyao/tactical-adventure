package com.jingyuyao.tactical.view.world.system;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.event.ActivatedEnemy;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.event.InstantMoveShip;
import com.jingyuyao.tactical.model.event.MoveShip;
import com.jingyuyao.tactical.model.event.Promise;
import com.jingyuyao.tactical.model.event.RemoveShip;
import com.jingyuyao.tactical.model.event.SpawnShip;
import com.jingyuyao.tactical.model.ship.Enemy;
import com.jingyuyao.tactical.model.ship.Player;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.state.PlayerState;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.model.world.Path;
import com.jingyuyao.tactical.view.world.component.Frame;
import com.jingyuyao.tactical.view.world.component.LoopAnimation;
import com.jingyuyao.tactical.view.world.component.Moving;
import com.jingyuyao.tactical.view.world.component.PlayerComponent;
import com.jingyuyao.tactical.view.world.component.Position;
import com.jingyuyao.tactical.view.world.component.Remove;
import com.jingyuyao.tactical.view.world.component.ShipComponent;
import com.jingyuyao.tactical.view.world.resource.Animations;
import com.jingyuyao.tactical.view.world.resource.Colors;
import com.jingyuyao.tactical.view.world.resource.Markers;
import com.jingyuyao.tactical.view.world.resource.WorldTexture;
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
  private Player player;
  @Mock
  private Enemy enemy;
  @Mock
  private WorldTexture texture;
  @Mock
  private SpawnShip spawnShip;
  @Mock
  private RemoveShip removeShip;
  @Mock
  private InstantMoveShip instantMoveShip;
  @Mock
  private MoveShip moveShip;
  @Mock
  private PlayerState playerState;
  @Mock
  private ActivatedEnemy activatedEnemy;
  @Mock
  private ExitState exitState;
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
  public void spawn_player() {
    LoopAnimation animation = new LoopAnimation(10, new WorldTexture[]{texture});
    when(spawnShip.getObject()).thenReturn(cell);
    when(cell.getCoordinate()).thenReturn(C1);
    when(cell.player()).thenReturn(Optional.of(player));
    when(cell.ship()).thenReturn(Optional.<Ship>of(player));
    when(player.getResourceKey()).thenReturn("me");
    when(animations.getShip("me")).thenReturn(animation);

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
    assertThat(frame.getColor()).isEqualTo(Colors.BLUE_300);
    LoopAnimation loopAnimation = entity.getComponent(LoopAnimation.class);
    assertThat(loopAnimation).isEqualTo(animation);
    ShipComponent shipComponent = entity.getComponent(ShipComponent.class);
    assertThat(shipComponent).isNotNull();
    assertThat(shipComponent.getShip()).isEqualTo(player);
    PlayerComponent playerComponent = entity.getComponent(PlayerComponent.class);
    assertThat(playerComponent).isNotNull();
    assertThat(playerComponent.getPlayer()).isEqualTo(player);
  }

  @Test
  public void spawn_enemy() {
    LoopAnimation animation = new LoopAnimation(10, new WorldTexture[]{texture});
    when(spawnShip.getObject()).thenReturn(cell);
    when(cell.getCoordinate()).thenReturn(C1);
    when(cell.enemy()).thenReturn(Optional.of(enemy));
    when(cell.player()).thenReturn(Optional.<Player>absent());
    when(cell.ship()).thenReturn(Optional.<Ship>of(enemy));
    when(enemy.getResourceKey()).thenReturn("me");
    when(animations.getShip("me")).thenReturn(animation);

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
    assertThat(frame.getColor()).isEqualTo(Colors.RED_500);
    LoopAnimation loopAnimation = entity.getComponent(LoopAnimation.class);
    assertThat(loopAnimation).isEqualTo(animation);
    ShipComponent shipComponent = entity.getComponent(ShipComponent.class);
    assertThat(shipComponent).isNotNull();
    assertThat(shipComponent.getShip()).isEqualTo(enemy);
    assertThat(entity.getComponent(PlayerComponent.class)).isNull();
  }

  @Test
  public void remove_ship() {
    spawn_enemy();
    when(removeShip.getObject()).thenReturn(enemy);

    shipSystem.removeShip(removeShip);

    ImmutableArray<Entity> entities = engine.getEntities();
    assertThat(entities).hasSize(1);
    Entity entity = entities.first();
    assertThat(entity.getComponent(Remove.class)).isNotNull();
  }

  @Test
  public void instant_move() {
    spawn_player();
    when(instantMoveShip.getShip()).thenReturn(player);
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
    spawn_player();
    when(moveShip.getShip()).thenReturn(player);
    when(moveShip.getPromise()).thenReturn(promise);
    when(moveShip.getPath()).thenReturn(path);
    when(path.getTrack()).thenReturn(ImmutableList.of(cell3, cell4, cell5, cell6));
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
    spawn_player();
    when(playerState.getPlayer()).thenReturn(player);
    when(markers.getActivated()).thenReturn(texture);

    shipSystem.playerState(playerState);

    ImmutableArray<Entity> entities = engine.getEntities();
    assertThat(entities).hasSize(1);
    Entity entity = entities.first();
    Frame frame = entity.getComponent(Frame.class);
    assertThat(frame).isNotNull();
    assertThat(frame.getOverlays()).containsExactly(texture);

    shipSystem.playerState(playerState);

    assertThat(frame.getOverlays()).containsExactly(texture);
  }

  @Test
  public void activated_enemy() {
    spawn_enemy();
    when(activatedEnemy.getObject()).thenReturn(enemy);
    when(markers.getActivated()).thenReturn(texture);

    shipSystem.activatedEnemy(activatedEnemy);

    ImmutableArray<Entity> entities = engine.getEntities();
    assertThat(entities).hasSize(1);
    Entity entity = entities.first();
    Frame frame = entity.getComponent(Frame.class);
    assertThat(frame).isNotNull();
    assertThat(frame.getOverlays()).containsExactly(texture);

    shipSystem.activatedEnemy(activatedEnemy);

    assertThat(frame.getOverlays()).containsExactly(texture);
  }

  @Test
  public void exit_state() {
    spawn_enemy();
    when(activatedEnemy.getObject()).thenReturn(enemy);
    when(markers.getActivated()).thenReturn(texture);

    shipSystem.activatedEnemy(activatedEnemy);

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