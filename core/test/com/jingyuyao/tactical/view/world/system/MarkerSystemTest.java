package com.jingyuyao.tactical.view.world.system;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.event.SelectCell;
import com.jingyuyao.tactical.model.state.Moving;
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
  private Moving moving;
  @Mock
  private Movement movement;
  @Mock
  private ExitState exitState;

  private PooledEngine engine;
  private MarkerSystem markerSystem;

  @Before
  public void setUp() {
    engine = new PooledEngine();
    markerSystem = new MarkerSystem(new ECF(engine), markers);
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

    ImmutableArray<Entity> entities = engine.getEntities();
    assertThat(entities).hasSize(2);
    Entity entity1 = entities.get(0);
    Frame frame1 = entity1.getComponent(Frame.class);
    Position position1 = entity1.getComponent(Position.class);
    assertThat(frame1).isNotNull();
    assertThat(frame1.getTexture()).hasValue(texture1);
    assertThat(position1).isNotNull();
    assertThat(position1.getX()).isEqualTo((float) C1.getX());
    assertThat(position1.getY()).isEqualTo((float) C1.getY());
    assertThat(position1.getZ()).isEqualTo(WorldZIndex.MOVE_MARKER);

    Entity entity2 = entities.get(1);
    Frame frame2 = entity2.getComponent(Frame.class);
    Position position2 = entity2.getComponent(Position.class);
    assertThat(frame2).isNotNull();
    assertThat(frame2.getTexture()).hasValue(texture1);
    assertThat(position2).isNotNull();
    assertThat(position2.getX()).isEqualTo((float) C2.getX());
    assertThat(position2.getY()).isEqualTo((float) C2.getY());
    assertThat(position2.getZ()).isEqualTo(WorldZIndex.MOVE_MARKER);

    // make sure the entities are tagged
    markerSystem.exitState(exitState);

    assertThat(entity1.getComponent(Remove.class)).isNotNull();
    assertThat(entity2.getComponent(Remove.class)).isNotNull();
  }
}