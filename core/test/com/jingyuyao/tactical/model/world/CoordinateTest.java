package com.jingyuyao.tactical.model.world;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

/**
 * Also contains a list of coordinate for other tests to use.
 */
public class CoordinateTest {

  public static final Coordinate C0_0 = new Coordinate(0, 0);
  public static final Coordinate C0_1 = new Coordinate(0, 1);
  public static final Coordinate C0_2 = new Coordinate(0, 2);
  public static final Coordinate C1_0 = new Coordinate(1, 0);
  public static final Coordinate C1_1 = new Coordinate(1, 1);
  public static final Coordinate C1_2 = new Coordinate(1, 2);
  public static final Coordinate C2_0 = new Coordinate(2, 0);
  public static final Coordinate C2_1 = new Coordinate(2, 1);
  public static final Coordinate C2_2 = new Coordinate(2, 2);

  @Test
  public void equality() {
    assertThat(C0_1).isEqualTo(new Coordinate(0, 1));
    assertThat(C0_1).isNotEqualTo(C2_2);
    assertThat(C0_1).isEqualTo(C0_1);
  }

  @Test
  public void hash() {
    assertThat(C0_1.hashCode()).isEqualTo(new Coordinate(0, 1).hashCode());
    assertThat(C0_1.hashCode()).isNotEqualTo(C2_2.hashCode());
    assertThat(C0_1.hashCode()).isEqualTo(C0_1.hashCode());
  }
}
