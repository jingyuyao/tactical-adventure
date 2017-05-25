package com.jingyuyao.tactical.model.battle;

import static com.google.common.truth.Truth.assertThat;

import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Direction;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
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
    target =
        new Target(
            origin, direction,
            Collections.singleton(cell1),
            new HashSet<>(Arrays.asList(cell1, cell2)));
  }

  @Test
  public void get_origin() {
    assertThat(target.getOrigin()).isSameAs(origin);
  }

  @Test
  public void get_direction() {
    assertThat(target.direction()).hasValue(direction);
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