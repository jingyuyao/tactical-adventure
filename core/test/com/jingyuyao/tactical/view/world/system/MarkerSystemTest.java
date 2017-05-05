package com.jingyuyao.tactical.view.world.system;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.jingyuyao.tactical.model.battle.Battle;
import com.jingyuyao.tactical.model.battle.Target;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.event.SelectCell;
import com.jingyuyao.tactical.model.state.Battling;
import com.jingyuyao.tactical.model.state.Moving;
import com.jingyuyao.tactical.model.state.SelectingTarget;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.model.world.Movement;
import com.jingyuyao.tactical.view.world.component.Frame;
import com.jingyuyao.tactical.view.world.component.Position;
import com.jingyuyao.tactical.view.world.component.Remove;
import com.jingyuyao.tactical.view.world.resource.Markers;
import com.jingyuyao.tactical.view.world.resource.WorldTexture;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MarkerSystemTest {

  private static final Coordinate C1 = new Coordinate(2, 3);
  private static final Coordinate C2 = new Coordinate(5, 4);

  @Mock
  private Markers markers;
  @Mock
  private Cell cell;
  @Mock
  private Cell cell2;
  @Mock
  private SelectCell selectCell;
  @Mock
  private WorldTexture texture1;
  @Mock
  private WorldTexture texture2;
  @Mock
  private Moving moving;
  @Mock
  private SelectingTarget selectingTarget;
  @Mock
  private Battling battling;
  @Mock
  private Battle battle;
  @Mock
  private Movement movement;
  @Mock
  private Target target;
  @Mock
  private ExitState exitState;

  private PooledEngine engine;
  private MarkerSystem markerSystem;

  @Before
  public void setUp() {
    engine = new PooledEngine();
    markerSystem = new MarkerSystem(markers);
    assertThat(markerSystem.priority).isEqualTo(SystemPriority.MARKER);
    engine.addSystem(markerSystem);
  }

  @Test
  public void select_cell() {
    when(markers.getHighlight()).thenReturn(texture1);
    when(selectCell.getObject()).thenReturn(cell, cell2);
    when(cell.getCoordinate()).thenReturn(C1);
    when(cell2.getCoordinate()).thenReturn(C2);

    markerSystem.selectCell(selectCell);

    ImmutableArray<Entity> entities = engine.getEntities();
    assertThat(entities).hasSize(1);
    Entity entity = entities.first();
    Frame frame = entity.getComponent(Frame.class);
    assertThat(frame).isNotNull();
    assertThat(frame.getTexture()).hasValue(texture1);

    Position position = entity.getComponent(Position.class);
    assertThat(position).isNotNull();
    assertThat(position.getX()).isEqualTo((float) C1.getX());
    assertThat(position.getY()).isEqualTo((float) C1.getY());
    assertThat(position.getZ()).isEqualTo(WorldZIndex.HIGHLIGHT_MARKER);

    // select another cell should use the old entity + frame but add a new position
    markerSystem.selectCell(selectCell);

    assertThat(entities).hasSize(1);
    assertThat(entities.first()).isSameAs(entity);
    assertThat(entity.getComponent(Frame.class)).isSameAs(frame);
    assertThat(frame.getTexture()).hasValue(texture1);

    position = entity.getComponent(Position.class);
    assertThat(position.getX()).isEqualTo((float) C2.getX());
    assertThat(position.getY()).isEqualTo((float) C2.getY());
    assertThat(position.getZ()).isEqualTo(WorldZIndex.HIGHLIGHT_MARKER);
  }

  @Test
  public void moving() {
    when(markers.getMove()).thenReturn(texture1);
    when(moving.getMovement()).thenReturn(movement);
    when(movement.getCells()).thenReturn(ImmutableList.of(cell, cell2));
    when(cell.getCoordinate()).thenReturn(C1);
    when(cell2.getCoordinate()).thenReturn(C2);

    markerSystem.moving(moving);

    assertContains(C1, WorldZIndex.MOVE_MARKER, texture1);
    assertContains(C2, WorldZIndex.MOVE_MARKER, texture1);

    // make sure the entities are tagged
    markerSystem.exitState(exitState);

    assertAllRemoved();
  }

  @Test
  public void selecting_target() {
    when(selectingTarget.getTargets()).thenReturn(ImmutableList.of(target));
    when(target.getSelectCells()).thenReturn(ImmutableSet.of(cell));
    when(target.getTargetCells()).thenReturn(ImmutableSet.of(cell2));
    when(cell.getCoordinate()).thenReturn(C1);
    when(cell2.getCoordinate()).thenReturn(C2);
    when(markers.getTargetSelect()).thenReturn(texture1);
    when(markers.getAttack()).thenReturn(texture2);

    markerSystem.selectingTarget(selectingTarget);

    assertContains(C1, WorldZIndex.TARGET_SELECT_MARKER, texture1);
    assertContains(C2, WorldZIndex.ATTACK_MARKER, texture2);

    markerSystem.exitState(exitState);

    assertAllRemoved();
  }

  @Test
  public void battling() {
    when(battling.getBattle()).thenReturn(battle);
    when(battle.getTarget()).thenReturn(target);
    when(target.getSelectCells()).thenReturn(ImmutableSet.of(cell));
    when(target.getTargetCells()).thenReturn(ImmutableSet.of(cell2));
    when(cell.getCoordinate()).thenReturn(C1);
    when(cell2.getCoordinate()).thenReturn(C2);
    when(markers.getTargetSelect()).thenReturn(texture1);
    when(markers.getAttack()).thenReturn(texture2);

    markerSystem.battling(battling);

    assertContains(C1, WorldZIndex.TARGET_SELECT_MARKER, texture1);
    assertContains(C2, WorldZIndex.ATTACK_MARKER, texture2);

    markerSystem.exitState(exitState);

    assertAllRemoved();
  }

  private void assertContains(
      final Coordinate coordinate,
      final int zIndex,
      final WorldTexture worldTexture) {
    assertThat(Iterables.any(engine.getEntities(), new Predicate<Entity>() {
      @Override
      public boolean apply(Entity input) {
        Position position = input.getComponent(Position.class);
        Frame frame = input.getComponent(Frame.class);
        return position != null
            && frame != null
            && position.getX() == (float) coordinate.getX()
            && position.getY() == (float) coordinate.getY()
            && position.getZ() == zIndex
            && frame.getTexture().isPresent()
            && frame.getTexture().get() == worldTexture;
      }
    })).isTrue();
  }

  private void assertAllRemoved() {
    assertThat(Iterables.all(engine.getEntities(), new Predicate<Entity>() {
      @Override
      public boolean apply(Entity input) {
        return input.getComponent(Remove.class) != null;
      }
    })).isTrue();
  }
}