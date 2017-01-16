package com.jingyuyao.tactical.model.item;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.battle.PiercingFactory;
import com.jingyuyao.tactical.model.battle.Target;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.common.Algorithms;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.common.Directions;
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
  @Mock
  private Player player;
  @Mock
  private Player player2;
  @Mock
  private Enemy enemy;
  @Mock
  private PiercingFactory piercingFactory;
  @Mock
  private Target target;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  private Weapon weapon;

  @Before
  public void setUp() {
    weapon =
        new Weapon(
            eventBus, NAME, INITIAL_USAGE, ATTACK_POWER, ATTACK_DISTANCES, algorithms, terrains,
            piercingFactory);
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
  public void can_target_same_character() {
    assertThat(weapon.canTarget(character, character)).isFalse();
  }

  @Test
  public void can_target_same_type() {
    assertThat(weapon.canTarget(player, player2)).isFalse();
  }

  @Test
  public void can_target_different_type() {
    assertThat(weapon.canTarget(player, enemy)).isTrue();
  }

  @Test
  public void can_hit_can_target_within_coordinate() {
    when(terrains.getWidth()).thenReturn(WIDTH);
    when(terrains.getHeight()).thenReturn(HEIGHT);
    when(algorithms.getNDistanceAway(WIDTH, HEIGHT, COORDINATE2, ATTACK_DISTANCE))
        .thenReturn(COORDINATE_LIST);
    when(enemy.getCoordinate()).thenReturn(COORDINATE);

    assertThat(weapon.canHitFrom(player, COORDINATE2, enemy)).isTrue();
  }

  @Test
  public void can_hit_can_target_not_within_coordiante() {
    when(terrains.getWidth()).thenReturn(WIDTH);
    when(terrains.getHeight()).thenReturn(HEIGHT);
    when(algorithms.getNDistanceAway(WIDTH, HEIGHT, COORDINATE2, ATTACK_DISTANCE))
        .thenReturn(ImmutableList.<Coordinate>of());
    when(enemy.getCoordinate()).thenReturn(COORDINATE);

    assertThat(weapon.canHitFrom(player, COORDINATE2, enemy)).isFalse();
  }

  @Test
  public void can_hit_same_character() {
    assertThat(weapon.canHitFrom(character, COORDINATE, character)).isFalse();
  }

  @Test
  public void can_hit_cannot_target() {
    assertThat(weapon.canHitFrom(player, COORDINATE, player2)).isFalse();
  }

  @Test
  public void hit() {
    weapon.hit(character);

    verify(character).damageBy(ATTACK_POWER);
    verifyZeroInteractions(eventBus);
  }

  @Test
  public void hit_twice_break() {
    weapon.hit(character);
    weapon.hit(character);

    verify(character, times(2)).damageBy(ATTACK_POWER);
    verify(eventBus).post(argumentCaptor.capture());
    RemoveItem removeItem = TestHelpers.isInstanceOf(argumentCaptor.getValue(), RemoveItem.class);
    assertThat(removeItem.getObject()).isSameAs(weapon);
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

  @Test
  public void get_targets() {
    when(piercingFactory.create(COORDINATE, Directions.UP)).thenReturn(Optional.<Target>absent());
    when(piercingFactory.create(COORDINATE, Directions.DOWN)).thenReturn(Optional.<Target>absent());
    when(piercingFactory.create(COORDINATE, Directions.LEFT)).thenReturn(Optional.<Target>absent());
    when(piercingFactory.create(COORDINATE, Directions.RIGHT)).thenReturn(Optional.of(target));

    assertThat(weapon.getTargets(COORDINATE)).containsExactly(target);
  }
}