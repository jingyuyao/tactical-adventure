package com.jingyuyao.tactical.model.resource;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class StringKeyTest {

  @Test
  public void equality() {
    KeyBundle bundle1 = new KeyBundle("some/path", "properties");
    KeyBundle bundle2 = new KeyBundle("some/other/path", null);
    StringKey stringKey1 = bundle1.get("abc");
    StringKey stringKey2 = bundle1.get("abc");
    StringKey stringKey3 = bundle2.get("abc");
    StringKey stringKey4 = bundle2.get("abc", 123);
    StringKey stringKey5 = bundle2.get("abc", 123);

    assertThat(stringKey1).isEqualTo(stringKey2);
    assertThat(stringKey2).isNotEqualTo(stringKey3);
    assertThat(stringKey3).isNotEqualTo(stringKey4);
    assertThat(stringKey4).isEqualTo(stringKey5);
    assertThat(stringKey5.format(123)).isEqualTo(stringKey5);
  }

  @Test
  public void hash() {
    KeyBundle bundle1 = new KeyBundle("some/path", "properties");
    KeyBundle bundle2 = new KeyBundle("some/other/path", null);
    StringKey stringKey1 = bundle1.get("abc");
    StringKey stringKey2 = bundle1.get("abc");
    StringKey stringKey3 = bundle2.get("abc");
    StringKey stringKey4 = bundle2.get("abc", 123);
    StringKey stringKey5 = bundle2.get("abc", 123);

    assertThat(stringKey1.hashCode()).isEqualTo(stringKey2.hashCode());
    assertThat(stringKey2.hashCode()).isNotEqualTo(stringKey3.hashCode());
    assertThat(stringKey3.hashCode()).isNotEqualTo(stringKey4.hashCode());
    assertThat(stringKey4.hashCode()).isEqualTo(stringKey5.hashCode());
    assertThat(stringKey5.format(123).hashCode()).isEqualTo(stringKey5.hashCode());
  }
}