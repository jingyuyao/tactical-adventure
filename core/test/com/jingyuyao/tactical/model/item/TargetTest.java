package com.jingyuyao.tactical.model.item;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.ImmutableSet;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.world.Cell;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TargetTest {

  @Mock
  private Cell cell1;
  @Mock
  private Cell cell2;
  @Mock
  private Character character;

  private Target target;

  @Before
  public void setUp() {
    target = new Target(ImmutableSet.of(cell1), ImmutableSet.of(cell1, cell2));
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
  public void get_target_terrains() {
    assertThat(target.getTargetCells()).containsExactly(cell1, cell2);
  }
}