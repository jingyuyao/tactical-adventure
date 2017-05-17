package com.jingyuyao.tactical.model.ship;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BasePlayerTest {

  private static final String NAME = "yo";

  @Mock
  private Stats stats;
  @Mock
  private Cockpit cockpit;
  @Mock
  private Items items;

  private Player player;

  @Before
  public void setUp() {
    player = new BasePlayer(NAME, true, stats, cockpit, items);
  }

  @Test
  public void get_actionable() {
    assertThat(player.isControllable()).isTrue();
  }

  @Test
  public void set_actionable() {
    player.setControllable(false);

    assertThat(player.isControllable()).isFalse();
  }
}