package com.jingyuyao.tactical.model.resource;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ResourceKeyTest {

  @Test
  public void equality() {
    ResourceKeyBundle bundle1 = new ResourceKeyBundle("some/path");
    ResourceKeyBundle bundle2 = new ResourceKeyBundle("some/other/path");
    ResourceKey resourceKey1 = bundle1.get("abc");
    ResourceKey resourceKey2 = bundle1.get("abc");
    ResourceKey resourceKey3 = bundle2.get("abc");
    ResourceKey resourceKey4 = bundle2.get("abc", 123);
    ResourceKey resourceKey5 = bundle2.get("abc", 123);

    assertThat(resourceKey1).isEqualTo(resourceKey2);
    assertThat(resourceKey2).isNotEqualTo(resourceKey3);
    assertThat(resourceKey3).isNotEqualTo(resourceKey4);
    assertThat(resourceKey4).isEqualTo(resourceKey5);
    assertThat(resourceKey5.format(123)).isEqualTo(resourceKey5);
  }

  @Test
  public void hash() {
    ResourceKeyBundle bundle1 = new ResourceKeyBundle("some/path");
    ResourceKeyBundle bundle2 = new ResourceKeyBundle("some/other/path");
    ResourceKey resourceKey1 = bundle1.get("abc");
    ResourceKey resourceKey2 = bundle1.get("abc");
    ResourceKey resourceKey3 = bundle2.get("abc");
    ResourceKey resourceKey4 = bundle2.get("abc", 123);
    ResourceKey resourceKey5 = bundle2.get("abc", 123);

    assertThat(resourceKey1.hashCode()).isEqualTo(resourceKey2.hashCode());
    assertThat(resourceKey2.hashCode()).isNotEqualTo(resourceKey3.hashCode());
    assertThat(resourceKey3.hashCode()).isNotEqualTo(resourceKey4.hashCode());
    assertThat(resourceKey4.hashCode()).isEqualTo(resourceKey5.hashCode());
    assertThat(resourceKey5.format(123).hashCode()).isEqualTo(resourceKey5.hashCode());
  }
}