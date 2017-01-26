package com.jingyuyao.tactical.model.item;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jingyuyao.tactical.model.character.Character;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BaseItemTest {

  private static final String NAME = "item";

  @Mock
  private Character owner;
  @Mock
  private ItemStats itemStats;

  private BaseItem<ItemStats> item;

  @Before
  public void setUp() {
    item = new BaseItem<ItemStats>(owner, itemStats);
  }

  @Test
  public void owner() {
    assertThat(item.getOwner()).isSameAs(owner);
  }

  @Test
  public void name() {
    when(itemStats.getName()).thenReturn(NAME);

    assertThat(item.getName()).isEqualTo(NAME);
  }

  @Test
  public void usage_left() {
    when(itemStats.getUsageLeft()).thenReturn(10);

    assertThat(item.getUsageLeft()).isEqualTo(10);
  }

  @Test
  public void use_once() {
    when(itemStats.getUsageLeft()).thenReturn(1);

    item.useOnce();

    verify(itemStats).decrementUsageLeft();
  }

  @Test
  public void use_once_broken() {
    when(itemStats.getUsageLeft()).thenReturn(0);

    item.useOnce();

    verify(itemStats).decrementUsageLeft();
    verify(owner).removeItem(item);
  }
}