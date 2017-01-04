package com.jingyuyao.tactical.model;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class CoordinateTest {
  private static final Coordinate C1 = new Coordinate(0, 1);
  private static final Coordinate C2 = new Coordinate(0, 1);
  private static final Coordinate C3 = new Coordinate(2, 2);

  @Test
  public void equality() {
    assertThat(C1).isEqualTo(C2);
    assertThat(C2).isNotEqualTo(C3);
    assertThat(C1).isEqualTo(C1);
  }

  @Test
  public void hash() {
    assertThat(C1.hashCode()).isEqualTo(C2.hashCode());
    assertThat(C2.hashCode()).isNotEqualTo(C3.hashCode());
    assertThat(C1.hashCode()).isEqualTo(C1.hashCode());
  }
}
