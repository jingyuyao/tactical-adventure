package com.jingyuyao.tactical.model.world;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.graph.ElementOrder;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MovementTest {

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
    MutableGraph<Cell> graph =
        GraphBuilder
            .directed()
            .allowsSelfLoops(false)
            .nodeOrder(ElementOrder.insertion())
            .build();
    graph.addNode(origin);
    graph.putEdge(origin, move1);
    graph.putEdge(move1, move2);
    movement = new Movement(graph);
  }

  @Test
  public void starting_coordinate() {
    assertThat(movement.getOrigin()).isSameAs(origin);
  }

  @Test
  public void can_move_to() {
    assertThat(movement.canMoveTo(origin)).isTrue();
    assertThat(movement.canMoveTo(move1)).isTrue();
    assertThat(movement.canMoveTo(move2)).isTrue();
    assertThat(movement.canMoveTo(other)).isFalse();
  }

  @Test
  public void move_cells() {
    assertThat(movement.getCells()).containsExactly(origin, move1, move2).inOrder();
  }

  @Test
  public void path_to() {
    Path path = movement.pathTo(move2);
    assertThat(path.getOrigin()).isSameAs(origin);
    assertThat(path.getDestination()).isSameAs(move2);
    assertThat(path.getTrack()).containsExactly(origin, move1, move2).inOrder();
  }
}