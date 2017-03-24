package com.jingyuyao.tactical.model.map;

import static com.google.common.truth.Truth.assertThat;

import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.terrain.Terrain;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CellTest {

  private static final Coordinate COORDINATE = new Coordinate(2, 3);

  @Mock
  private Terrain terrain;
  @Mock
  private Player player;
  @Mock
  private Enemy enemy;

  private Cell cell;

  @Before
  public void setUp() {
    cell = new Cell(COORDINATE, terrain);
    assertThat(cell.getCoordinate()).isEqualTo(COORDINATE);
    assertThat(cell.getTerrain()).isSameAs(terrain);
    assertThat(cell.hasCharacter()).isFalse();
    assertThat(cell.hasPlayer()).isFalse();
    assertThat(cell.hasEnemy()).isFalse();
  }

  @Test
  public void get_has_player() {
    cell.setCharacter(player);

    assertThat(cell.hasCharacter()).isTrue();
    assertThat(cell.hasPlayer()).isTrue();
    assertThat(cell.getPlayer()).isSameAs(player);
    assertThat(cell.hasEnemy()).isFalse();
  }

  @Test
  public void get_has_enemy() {
    cell.setCharacter(enemy);

    assertThat(cell.hasCharacter()).isTrue();
    assertThat(cell.hasEnemy()).isTrue();
    assertThat(cell.getEnemy()).isSameAs(enemy);
    assertThat(cell.hasPlayer()).isFalse();
  }

  @Test
  public void move_character() {
    Cell other = new Cell(COORDINATE, terrain);
    cell.setCharacter(player);

    cell.moveCharacterTo(other);

    assertThat(cell.hasCharacter()).isFalse();
    assertThat(other.hasCharacter()).isTrue();
    assertThat(other.getCharacter()).isSameAs(player);
  }
}