package com.jingyuyao.tactical.model;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class IdentifiableTest {

  @Test
  public void get_and_has() {
    Identifiable identifiable = new Identifiable();

    assertThat(identifiable.hasID(identifiable.getID())).isTrue();
  }

  @Test
  public void equals_and_hash() {
    Identifiable identifiable1 = new Identifiable();
    Identifiable identifiable2 = new Identifiable();

    assertThat(identifiable1).isEqualTo(identifiable1);
    assertThat(identifiable1).isNotEqualTo(identifiable2);
    assertThat(identifiable1.hashCode()).isEqualTo(identifiable1.getID().hashCode());
    assertThat(identifiable2.hashCode()).isEqualTo(identifiable2.getID().hashCode());
  }
}