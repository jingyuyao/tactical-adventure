package com.jingyuyao.tactical.model.item;

import static com.google.common.truth.Truth.assertThat;

import com.jingyuyao.tactical.model.i18n.Message;
import com.jingyuyao.tactical.model.i18n.ModelBundle;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BaseItemTest {

  private static final String NAME_KEY = "item_name";
  private static final String RESOURCE_KEY = "item_resource";
  private static final int USAGE_LEFT = 1;

  private BaseItem item;

  @Before
  public void setUp() {
    item = new BaseItem(NAME_KEY, RESOURCE_KEY, USAGE_LEFT);
  }

  @Test
  public void get_resource_key() {
    assertThat(item.getResourceKey()).isEqualTo(RESOURCE_KEY);
  }

  @Test
  public void get_name() {
    Message message = item.getName();
    assertThat(message.getBundle()).isSameAs(ModelBundle.ITEM_NAME);
    assertThat(message.getKey()).isEqualTo(NAME_KEY);
  }

  @Test
  public void get_description() {
    Message message = item.getDescription();
    assertThat(message.getBundle()).isSameAs(ModelBundle.ITEM_DESCRIPTION);
    assertThat(message.getKey()).isEqualTo(NAME_KEY);
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