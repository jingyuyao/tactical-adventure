package com.jingyuyao.tactical.view.world.system;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.event.ActivatedEnemy;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.event.InstantMoveCharacter;
import com.jingyuyao.tactical.model.event.MoveCharacter;
import com.jingyuyao.tactical.model.event.MyFuture;
import com.jingyuyao.tactical.model.event.RemoveCharacter;
import com.jingyuyao.tactical.model.event.SpawnCharacter;
import com.jingyuyao.tactical.model.state.PlayerState;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.model.world.Path;
import com.jingyuyao.tactical.view.world.component.CharacterComponent;
import com.jingyuyao.tactical.view.world.component.Frame;
import com.jingyuyao.tactical.view.world.component.LoopAnimation;
import com.jingyuyao.tactical.view.world.component.Moving;
import com.jingyuyao.tactical.view.world.component.PlayerComponent;
import com.jingyuyao.tactical.view.world.component.Position;
import com.jingyuyao.tactical.view.world.component.Remove;
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
public class CharacterSystemTest {

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
  private SpawnCharacter spawnCharacter;
  @Mock
  private RemoveCharacter removeCharacter;
  @Mock
  private InstantMoveCharacter instantMoveCharacter;
  @Mock
  private MoveCharacter moveCharacter;
  @Mock
  private PlayerState playerState;
  @Mock
  private ActivatedEnemy activatedEnemy;
  @Mock
  private ExitState exitState;
  @Mock
  private Path path;
  @Mock
  private MyFuture future;

  private Engine engine;
  private CharacterSystem characterSystem;

  @Before
  public void setUp() {
    engine = new PooledEngine();
    characterSystem =
        new CharacterSystem(
            markers,
            animations,
            ComponentMapper.getFor(CharacterComponent.class),
            ComponentMapper.getFor(Frame.class));
    assertThat(characterSystem.priority).isEqualTo(SystemPriority.CHARACTER);
    engine.addSystem(characterSystem);
  }

  @Test
  public void spawn_player() {
    LoopAnimation animation = new LoopAnimation(10, new WorldTexture[]{texture});
    when(spawnCharacter.getObject()).thenReturn(cell);
    when(cell.getCoordinate()).thenReturn(C1);
    when(cell.hasPlayer()).thenReturn(true);
    when(cell.getPlayer()).thenReturn(player);
    when(cell.getCharacter()).thenReturn(player);
    when(player.getName()).thenReturn("me");
    when(animations.getCharacter("me")).thenReturn(animation);

    characterSystem.spawnCharacter(spawnCharacter);

    ImmutableArray<Entity> entities = engine.getEntities();
    assertThat(entities).hasSize(1);
    Entity entity = entities.first();
    Position position = entity.getComponent(Position.class);
    assertThat(position).isNotNull();
    assertThat(position.getX()).isEqualTo((float) C1.getX());
    assertThat(position.getY()).isEqualTo((float) C1.getY());
    assertThat(position.getZ()).isEqualTo(WorldZIndex.CHARACTER);
    Frame frame = entity.getComponent(Frame.class);
    assertThat(frame).isNotNull();
    assertThat(frame.getColor()).isEqualTo(Colors.BLUE_300);
    LoopAnimation loopAnimation = entity.getComponent(LoopAnimation.class);
    assertThat(loopAnimation).isEqualTo(animation);
    CharacterComponent characterComponent = entity.getComponent(CharacterComponent.class);
    assertThat(characterComponent).isNotNull();
    assertThat(characterComponent.getCharacter()).isEqualTo(player);
    PlayerComponent playerComponent = entity.getComponent(PlayerComponent.class);
    assertThat(playerComponent).isNotNull();
    assertThat(playerComponent.getPlayer()).isEqualTo(player);
  }

  @Test
  public void spawn_enemy() {
    LoopAnimation animation = new LoopAnimation(10, new WorldTexture[]{texture});
    when(spawnCharacter.getObject()).thenReturn(cell);
    when(cell.getCoordinate()).thenReturn(C1);
    when(cell.hasEnemy()).thenReturn(true);
    when(cell.getCharacter()).thenReturn(enemy);
    when(enemy.getName()).thenReturn("me");
    when(animations.getCharacter("me")).thenReturn(animation);

    characterSystem.spawnCharacter(spawnCharacter);

    ImmutableArray<Entity> entities = engine.getEntities();
    assertThat(entities).hasSize(1);
    Entity entity = entities.first();
    Position position = entity.getComponent(Position.class);
    assertThat(position).isNotNull();
    assertThat(position.getX()).isEqualTo((float) C1.getX());
    assertThat(position.getY()).isEqualTo((float) C1.getY());
    assertThat(position.getZ()).isEqualTo(WorldZIndex.CHARACTER);
    Frame frame = entity.getComponent(Frame.class);
    assertThat(frame).isNotNull();
    assertThat(frame.getColor()).isEqualTo(Colors.RED_500);
    LoopAnimation loopAnimation = entity.getComponent(LoopAnimation.class);
    assertThat(loopAnimation).isEqualTo(animation);
    CharacterComponent characterComponent = entity.getComponent(CharacterComponent.class);
    assertThat(characterComponent).isNotNull();
    assertThat(characterComponent.getCharacter()).isEqualTo(enemy);
    assertThat(entity.getComponent(PlayerComponent.class)).isNull();
  }

  @Test
  public void remove_character() {
    spawn_enemy();
    when(removeCharacter.getObject()).thenReturn(enemy);

    characterSystem.removeCharacter(removeCharacter);

    ImmutableArray<Entity> entities = engine.getEntities();
    assertThat(entities).hasSize(1);
    Entity entity = entities.first();
    assertThat(entity.getComponent(Remove.class)).isNotNull();
  }

  @Test
  public void instant_move() {
    spawn_player();
    when(instantMoveCharacter.getCharacter()).thenReturn(player);
    when(instantMoveCharacter.getDestination()).thenReturn(cell2);
    when(cell2.getCoordinate()).thenReturn(C2);

    characterSystem.instantMoveCharacter(instantMoveCharacter);

    ImmutableArray<Entity> entities = engine.getEntities();
    assertThat(entities).hasSize(1);
    Entity entity = entities.first();
    Position position = entity.getComponent(Position.class);
    assertThat(position).isNotNull();
    assertThat(position.getX()).isEqualTo((float) C2.getX());
    assertThat(position.getY()).isEqualTo((float) C2.getY());
    assertThat(position.getZ()).isEqualTo(WorldZIndex.CHARACTER);
  }

  @Test
  public void move() {
    spawn_player();
    when(moveCharacter.getCharacter()).thenReturn(player);
    when(moveCharacter.getFuture()).thenReturn(future);
    when(moveCharacter.getPath()).thenReturn(path);
    when(path.getTrack()).thenReturn(ImmutableList.of(cell3, cell4, cell5, cell6));
    when(cell3.getCoordinate()).thenReturn(T1);
    when(cell4.getCoordinate()).thenReturn(T2);
    when(cell5.getCoordinate()).thenReturn(T3);
    when(cell6.getCoordinate()).thenReturn(T4);

    characterSystem.moveCharacter(moveCharacter);

    ImmutableArray<Entity> entities = engine.getEntities();
    assertThat(entities).hasSize(1);
    Entity entity = entities.first();
    Moving moving = entity.getComponent(Moving.class);
    assertThat(moving).isNotNull();
    assertThat(moving.getCurrentIndex()).isEqualTo(0);
    assertThat(moving.getFuture()).isSameAs(future);
    assertThat(moving.getPath()).containsExactly(T3, T4).inOrder();
  }

  @Test
  public void player_state() {
    spawn_player();
    when(playerState.getPlayer()).thenReturn(player);
    when(markers.getActivated()).thenReturn(texture);

    characterSystem.playerState(playerState);

    ImmutableArray<Entity> entities = engine.getEntities();
    assertThat(entities).hasSize(1);
    Entity entity = entities.first();
    Frame frame = entity.getComponent(Frame.class);
    assertThat(frame).isNotNull();
    assertThat(frame.getOverlays()).containsExactly(texture);

    characterSystem.playerState(playerState);

    assertThat(frame.getOverlays()).containsExactly(texture);
  }

  @Test
  public void activated_enemy() {
    spawn_enemy();
    when(activatedEnemy.getObject()).thenReturn(enemy);
    when(markers.getActivated()).thenReturn(texture);

    characterSystem.activatedEnemy(activatedEnemy);

    ImmutableArray<Entity> entities = engine.getEntities();
    assertThat(entities).hasSize(1);
    Entity entity = entities.first();
    Frame frame = entity.getComponent(Frame.class);
    assertThat(frame).isNotNull();
    assertThat(frame.getOverlays()).containsExactly(texture);

    characterSystem.activatedEnemy(activatedEnemy);

    assertThat(frame.getOverlays()).containsExactly(texture);
  }

  @Test
  public void exit_state() {
    spawn_enemy();
    when(activatedEnemy.getObject()).thenReturn(enemy);
    when(markers.getActivated()).thenReturn(texture);

    characterSystem.activatedEnemy(activatedEnemy);

    ImmutableArray<Entity> entities = engine.getEntities();
    assertThat(entities).hasSize(1);
    Entity entity = entities.first();
    Frame frame = entity.getComponent(Frame.class);
    assertThat(frame).isNotNull();
    assertThat(frame.getOverlays()).containsExactly(texture);

    characterSystem.exitState(exitState);

    assertThat(frame.getOverlays()).isEmpty();
  }
}