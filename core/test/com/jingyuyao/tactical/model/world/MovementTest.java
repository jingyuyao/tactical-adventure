package com.jingyuyao.tactical.model.world;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableSet;
import com.google.common.graph.Graph;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

// TODO: test path_to
@RunWith(MockitoJUnitRunner.class)
public class MovementTest {

  @Mock
  private Graph<Cell> graph;
  @Mock
  private Cell origin;
  @Mock
  private Cell move1;
  @Mock
  private Cell move2;
  @Mock
  private Cell other;

  private Movement movement;

  @Before
  public void setUp() {
    when(graph.nodes()).thenReturn(ImmutableSet.of(origin, move1, move2));
    movement = new Movement(graph);
  }

  @Test
  public void starting_coordinate() {
    assertThat(movement.getStartingCell()).isSameAs(origin);
  }

  @Test
  public void can_move_to() {
    assertThat(movement.canMoveTo(move1)).isTrue();
    assertThat(movement.canMoveTo(move2)).isTrue();
    assertThat(movement.canMoveTo(other)).isFalse();
  }

  @Test
  public void move_cells() {
    assertThat(movement.getCells()).containsExactly(origin, move1, move2).inOrder();
  }
}