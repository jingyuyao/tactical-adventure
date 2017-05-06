package com.jingyuyao.tactical.model.character;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BasePlayerTest {

  private static final String NAME_KEY = "yo";
  private static final String RESOURCE_KEY = "yo_face";
  private static final int MAX_HP = 20;
  private static final int HP = 10;
  private static final int MOVE_DISTANCE = 3;

  @Mock
  private Items items;

  private Player player;

  @Before
  public void setUp() {
    player = new BasePlayer(NAME_KEY, RESOURCE_KEY, MAX_HP, HP, MOVE_DISTANCE, items, true);
  }

  @Test
  public void get_actionable() {
    assertThat(player.canControl()).isTrue();
  }

  @Test
  public void set_actionable() {
    player.setActionable(false);

    assertThat(player.canControl()).isFalse();
  }
}