package com.jingyuyao.tactical.model.item;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.ImmutableSet;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Direction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TargetTest {

  @Mock
  private Cell origin;
  @Mock
  private Cell cell1;
  @Mock
  private Cell cell2;

  private Direction direction = Direction.RIGHT;
  private Target target;

  @Before
  public void setUp() {
    target = new Target(origin, direction, ImmutableSet.of(cell1), ImmutableSet.of(cell1, cell2));
  }

  @Test
  public void get_origin() {
    assertThat(target.getOrigin()).isSameAs(origin);
  }

  @Test
  public void get_direction() {
    assertThat(target.getDirection()).hasValue(direction);
  }

  @Test
  public void selected_by() {
    assertThat(target.selectedBy(cell1)).isTrue();
    assertThat(target.selectedBy(cell2)).isFalse();
  }

  @Test
  public void get_select_cells() {
    assertThat(target.getSelectCells()).containsExactly(cell1);
  }

  @Test
  public void get_target_cells() {
    assertThat(target.getTargetCells()).containsExactly(cell1, cell2);
  }
}