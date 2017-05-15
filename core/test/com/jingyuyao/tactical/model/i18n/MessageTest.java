package com.jingyuyao.tactical.model.i18n;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class MessageTest {

  @Test
  public void equality() {
    MessageBundle bundle1 = new MessageBundle("some/path");
    MessageBundle bundle2 = new MessageBundle("some/other/path");
    Message message1 = bundle1.get("abc");
    Message message2 = bundle1.get("abc");
    Message message3 = bundle2.get("abc");
    Message message4 = bundle2.get("abc", 123);
    Message message5 = bundle2.get("abc", 123);

    assertThat(message1).isEqualTo(message2);
    assertThat(message2).isNotEqualTo(message3);
    assertThat(message3).isNotEqualTo(message4);
    assertThat(message4).isEqualTo(message5);
    assertThat(message5.format(123)).isEqualTo(message5);
  }

  @Test
  public void hash() {
    MessageBundle bundle1 = new MessageBundle("some/path");
    MessageBundle bundle2 = new MessageBundle("some/other/path");
    Message message1 = bundle1.get("abc");
    Message message2 = bundle1.get("abc");
    Message message3 = bundle2.get("abc");
    Message message4 = bundle2.get("abc", 123);
    Message message5 = bundle2.get("abc", 123);

    assertThat(message1.hashCode()).isEqualTo(message2.hashCode());
    assertThat(message2.hashCode()).isNotEqualTo(message3.hashCode());
    assertThat(message3.hashCode()).isNotEqualTo(message4.hashCode());
    assertThat(message4.hashCode()).isEqualTo(message5.hashCode());
    assertThat(message5.format(123).hashCode()).isEqualTo(message5.hashCode());
  }
}