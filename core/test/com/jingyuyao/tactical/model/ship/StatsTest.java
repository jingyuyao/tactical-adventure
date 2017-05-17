package com.jingyuyao.tactical.model.ship;

import static com.google.common.truth.Truth.assertThat;

import com.jingyuyao.tactical.model.Allegiance;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class StatsTest {

  private static final int MAX_HP = 20;
  private static final int HP = 10;
  private static final int MOVE_DISTANCE = 3;

  private Stats stats;

  @Before
  public void setUp() {
    stats = new Stats(MAX_HP, HP, MOVE_DISTANCE);
  }

  @Test
  public void get_allegiance() {
    assertThat(stats.getAllegiance()).isSameAs(Allegiance.ENEMY);
  }

  @Test
  public void is_controllable() {
    assertThat(stats.isControllable()).isFalse();
  }

  @Test
  public void set_controllable() {
    stats.setControllable(true);

    assertThat(stats.isControllable()).isTrue();
  }

  @Test
  public void damage_by() {
    stats.damageBy(5);

    assertThat(stats.getHp()).isEqualTo(HP - 5);

    stats.damageBy(1000);

    assertThat(stats.getHp()).isEqualTo(0);
  }

  @Test
  public void heal_by() {
    stats.healBy(5);

    assertThat(stats.getHp()).isEqualTo(HP + 5);

    stats.healBy(1000);

    assertThat(stats.getHp()).isEqualTo(MAX_HP);
  }

  @Test
  public void full_heal() {
    stats.fullHeal();

    assertThat(stats.getHp()).isEqualTo(MAX_HP);
  }
}