package com.jingyuyao.tactical.model.item;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.common.Algorithms;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.item.event.RemoveItem;
import com.jingyuyao.tactical.model.map.Terrains;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WeaponTest {

  private static final String NAME = "ok";
  private static final int INITIAL_USAGE = 2;
  private static final int ATTACK_POWER = 5;
  private static final int WIDTH = 5;
  private static final int HEIGHT = 6;
  private static final Coordinate COORDINATE = new Coordinate(10, 10);
  private static final Coordinate COORDINATE2 = new Coordinate(11, 12);
  private static final ImmutableList<Coordinate> COORDINATE_LIST =
      ImmutableList.of(COORDINATE);
  private static final int ATTACK_DISTANCE = 3;
  private static final Set<Integer> ATTACK_DISTANCES = ImmutableSet.of(ATTACK_DISTANCE);

  @Mock
  private EventBus eventBus;
  @Mock
  private Algorithms algorithms;
  @Mock
  private Terrains terrains;
  @Mock
  private Character character;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  private Weapon weapon;

  @Before
  public void setUp() {
    weapon =
        new Weapon(
            eventBus, NAME, INITIAL_USAGE, ATTACK_POWER, ATTACK_DISTANCES, algorithms, terrains);
  }

  @Test
  public void name() {
    assertThat(weapon.getName()).isEqualTo(NAME);
  }

  @Test
  public void get_attack_distances() {
    assertThat(weapon.getAttackDistances()).isSameAs(ATTACK_DISTANCES);
  }

  @Test
  public void hit() {
    weapon.hit(character);

    verify(character).damageBy(ATTACK_POWER);
  }

  @Test
  public void target_coordinates_for() {
    when(terrains.getWidth()).thenReturn(WIDTH);
    when(terrains.getHeight()).thenReturn(HEIGHT);
    when(algorithms.getNDistanceAway(WIDTH, HEIGHT, COORDINATE2, ATTACK_DISTANCE))
        .thenReturn(COORDINATE_LIST);

    assertThat(weapon.targetCoordinatesFor(COORDINATE2)).containsExactly(COORDINATE);
  }

  @Test
  public void get_attack_power() {
    assertThat(weapon.getAttackPower()).isEqualTo(ATTACK_POWER);
  }

  @Test
  public void use() {
    weapon.useOnce();

    verifyZeroInteractions(eventBus);

    weapon.useOnce();
    verify(eventBus).post(argumentCaptor.capture());
    RemoveItem removeItem = TestHelpers.isInstanceOf(argumentCaptor.getValue(), RemoveItem.class);
    assertThat(removeItem.getObject()).isSameAs(weapon);
  }
}