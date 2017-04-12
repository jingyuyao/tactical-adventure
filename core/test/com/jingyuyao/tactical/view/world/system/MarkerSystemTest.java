package com.jingyuyao.tactical.view.world.system;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.jingyuyao.tactical.model.event.SelectCell;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.view.world.component.Frame;
import com.jingyuyao.tactical.view.world.component.Position;
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
}