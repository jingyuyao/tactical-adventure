package com.jingyuyao.tactical.model.map;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.graph.Graph;
import com.jingyuyao.tactical.model.common.Algorithms;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.mark.MarkingFactory;
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
  private Algorithms algorithms;
  @Mock
  private Terrains terrains;
  @Mock
  private Graph<Coordinate> graph;
  @Mock
  private Set<Coordinate> moveCoordinates;
  @Mock
  private ImmutableList<Coordinate> track;
  @Mock
  private MarkingFactory markingFactory;

  private Movement movement;

  @Before
  public void setUp() {
    when(graph.nodes()).thenReturn(ImmutableSet.of(ORIGIN, MOVE1, MOVE2));
    movement = new Movement(algorithms, terrains, graph, markingFactory);
  }

  @Test
  public void can_move_to() {
    assertThat(movement.canMoveTo(MOVE1)).isTrue();
    assertThat(movement.canMoveTo(TARGET1)).isFalse();
    assertThat(movement.canMoveTo(NOT_IN_TARGETS)).isFalse();
  }

  @Test
  public void path_to() {
    when(algorithms.getTrackTo(graph, MOVE1)).thenReturn(track);

    Path path = movement.pathTo(MOVE1);

    assertThat(path.getTrack()).isSameAs(track);
    assertThat(path.getDestination()).isEqualTo(MOVE1);
  }

  @Test
  public void move_terrains() {
    when(graph.nodes()).thenReturn(moveCoordinates);

    assertThat(movement.getCoordinates()).isSameAs(moveCoordinates);
  }
}