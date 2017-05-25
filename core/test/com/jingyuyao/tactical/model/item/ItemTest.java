package com.jingyuyao.tactical.model.item;

import static com.google.common.truth.Truth.assertThat;

import com.jingyuyao.tactical.model.resource.ModelBundle;
import com.jingyuyao.tactical.model.resource.StringKey;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ItemTest {

  private static final String name = "item_name";
  private static final int USAGE_LEFT = 1;

  private Item item;

  @Before
  public void setUp() {
    item = new Item(name, USAGE_LEFT);
  }

  @Test
  public void get_resource_key() {
    StringKey stringKey = item.getAnimation();
    assertThat(stringKey.getBundle()).isSameAs(ModelBundle.WEAPON_ANIMATIONS);
    assertThat(stringKey.getId()).isEqualTo(name);
  }

  @Test
  public void get_name() {
    StringKey stringKey = item.getName();
    assertThat(stringKey.getBundle()).isSameAs(ModelBundle.ITEM_NAME);
    assertThat(stringKey.getId()).isEqualTo(name);
  }

  @Test
  public void get_description() {
    StringKey stringKey = item.getDescription();
    assertThat(stringKey.getBundle()).isSameAs(ModelBundle.ITEM_DESCRIPTION);
    assertThat(stringKey.getId()).isEqualTo(name);
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