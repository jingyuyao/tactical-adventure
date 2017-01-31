package com.jingyuyao.tactical.model.map;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.graph.Graph;
import com.jingyuyao.tactical.model.terrain.Terrain;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MovementTest {

  private static final Coordinate ORIGIN = new Coordinate(5, 5);
  private static final Coordinate MOVE1 = new Coordinate(0, 0);
  private static final Coordinate MOVE2 = new Coordinate(0, 1);
  private static final Coordinate TARGET1 = new Coordinate(0, 2);
  private static final Coordinate NOT_IN_TARGETS = new Coordinate(10, 10);

  @Mock
  private Terrains terrains;
  @Mock
  private Graph<Coordinate> graph;
  @Mock
  private Terrain terrain;

  private Set<Coordinate> moveCoordinates;
  private Movement movement;

  @Before
  public void setUp() {
    moveCoordinates = ImmutableSet.of(ORIGIN, MOVE1, MOVE2);
    when(graph.nodes()).thenReturn(moveCoordinates);
    movement = new Movement(terrains, graph);
  }

  @Test
  public void can_move_to() {
    assertThat(movement.canMoveTo(MOVE1)).isTrue();
    assertThat(movement.canMoveTo(TARGET1)).isFalse();
    assertThat(movement.canMoveTo(NOT_IN_TARGETS)).isFalse();
  }

  @Test
  public void path_to() {
    // TODO: finish me
//    Path path = movement.pathTo(MOVE1);
//
//    assertThat(path.getDestination()).isEqualTo(MOVE1);
  }

  @Test
  public void move_terrains() {
    assertThat(movement.getCoordinates()).containsExactly(ORIGIN, MOVE1, MOVE2);
  }

  @Test
  public void show_marking() {
    when(terrains.getAll(moveCoordinates)).thenReturn(ImmutableList.of(terrain));

    movement.showMarking();

    verify(terrain).addMarker(Marker.CAN_MOVE_TO);
  }

  @Test
  public void hide_marking() {
    show_marking();

    movement.hideMarking();

    verify(terrain).removeMarker(Marker.CAN_MOVE_TO);
  }
}