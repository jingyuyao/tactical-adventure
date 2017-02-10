package com.jingyuyao.tactical.model.item;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BaseItemTest {

  private static final String NAME = "item";
  private static final int USAGE_LEFT = 1;

  private BaseItem item;

  @Before
  public void setUp() {
    item = new BaseItem(NAME, USAGE_LEFT) {
      @Override
      public String getDescription() {
        return null;
      }
    };
  }

  @Test
  public void name() {
    assertThat(item.getName()).isEqualTo(NAME);
  }

  @Test
  public void usage_left() {
    assertThat(item.getUsageLeft()).isEqualTo(USAGE_LEFT);
  }

  @Test
  public void use_once() {
    item.useOnce();

    assertThat(item.getUsageLeft()).isEqualTo(0);
  }
}