package com.jingyuyao.tactical.model.map;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.SetMultimap;
import com.google.common.graph.Graph;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.common.Algorithms;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.item.Weapon;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TargetsTest {

  private static final Coordinate ORIGIN = new Coordinate(5, 5);
  private static final Coordinate ORIGIN_TARGET = new Coordinate(6, 6);
  private static final Coordinate MOVE1 = new Coordinate(0, 0);
  private static final Coordinate MOVE2 = new Coordinate(0, 1);
  private static final Coordinate TARGET1 = new Coordinate(0, 2);
  private static final Coordinate TARGET2 = new Coordinate(0, 3);
  private static final Coordinate NOT_IN_TARGETS = new Coordinate(10, 10);

  @Mock
  private Algorithms algorithms;
  @Mock
  private Characters characters;
  @Mock
  private Terrains terrains;
  @Mock
  private Character character;
  @Mock
  private Graph<Coordinate> graph;
  @Mock
  private Character other;
  @Mock
  private Weapon originWeapon;
  @Mock
  private Weapon weapon1;
  @Mock
  private Weapon weapon2;
  @Mock
  private ImmutableList<Coordinate> track;
  @Mock
  private Iterator<Character> characterIterator;
  @Mock
  private Iterable<Terrain> terrainIterable;

  private Map<Coordinate, SetMultimap<Coordinate, Weapon>> moveMap;
  private Targets targets;

  @Before
  public void setUp() {
    when(graph.nodes()).thenReturn(ImmutableSet.of(ORIGIN, MOVE1, MOVE2));
    when(character.getCoordinate()).thenReturn(ORIGIN);
    moveMap = createMoveMap();
    targets = new Targets(algorithms, characters, terrains, character, graph, moveMap);
  }

  @Test
  public void can_target_after_move() {
    when(character.canTarget(other)).thenReturn(true);
    when(other.getCoordinate()).thenReturn(TARGET1);

    assertThat(targets.all().canTarget(other)).isTrue();
  }

  @Test
  public void cannot_target_after_move() {
    when(character.canTarget(other)).thenReturn(true);
    when(other.getCoordinate()).thenReturn(NOT_IN_TARGETS);

    assertThat(targets.all().canTarget(other)).isFalse();
  }

  @Test
  public void target_after_move_cannot_target() {
    when(character.canTarget(other)).thenReturn(false);

    assertThat(targets.all().canTarget(other)).isFalse();
  }

  @Test
  public void can_target_immediately() {
    when(character.canTarget(other)).thenReturn(true);
    when(other.getCoordinate()).thenReturn(ORIGIN_TARGET);

    assertThat(targets.immediate().canTarget(other)).isTrue();
  }

  @Test
  public void cannot_target_immediately() {
    when(character.canTarget(other)).thenReturn(true);
    when(other.getCoordinate()).thenReturn(NOT_IN_TARGETS);

    assertThat(targets.immediate().canTarget(other)).isFalse();
  }

  @Test
  public void immediate_target_cannot_target() {
    when(character.canTarget(other)).thenReturn(false);

    assertThat(targets.immediate().canTarget(other)).isFalse();
  }

  @Test
  public void can_move_to() {
    assertThat(targets.canMoveTo(MOVE1)).isTrue();
    assertThat(targets.canMoveTo(TARGET1)).isFalse();
    assertThat(targets.canMoveTo(NOT_IN_TARGETS)).isFalse();
  }

  @Test
  public void path_to() {
    when(algorithms.getTrackTo(graph, MOVE1)).thenReturn(track);

    Path path = targets.pathTo(MOVE1);

    assertThat(path.getTrack()).isSameAs(track);
    assertThat(path.getDestination()).isEqualTo(MOVE1);
  }

  @Test
  public void move_path_to_target_max_number_of_weapons() {
    when(algorithms.getTrackTo(graph, MOVE2)).thenReturn(track);

    Path path = targets.movePathToTargetCoordinate(TARGET2);

    assertThat(path.getDestination()).isEqualTo(MOVE2);
    assertThat(path.getTrack()).isSameAs(track);
  }

  @Test
  public void all_targets() {
    assertThat(targets.all().coordinates()).containsExactly(TARGET1, TARGET2, ORIGIN_TARGET, MOVE1);
  }

  @Test
  public void immediate_targets() {
    assertThat(targets.immediate().coordinates()).containsExactly(ORIGIN_TARGET);
  }

  @Test
  public void weapons_for() {
    ImmutableSet<Weapon> weapons1 = targets.availableWeapons(MOVE1, TARGET1);
    assertThat(weapons1).containsExactly(weapon1);

    ImmutableSet<Weapon> weapons2 = targets.availableWeapons(MOVE2, TARGET2);
    assertThat(weapons2).containsExactly(weapon1, weapon2);

    assertThat(targets.availableWeapons(ORIGIN, TARGET1)).isEmpty();
  }

  @Test
  public void all_target_characters() {
    when(character.canTarget(other)).thenReturn(true);
    when(characters.iterator()).thenReturn(characterIterator);
    when(characterIterator.hasNext()).thenReturn(true, false);
    when(characterIterator.next()).thenReturn(other);
    when(other.getCoordinate()).thenReturn(TARGET1);

    assertThat(targets.all().characters()).containsExactly(other);
  }

  @Test
  public void all_target_characters_cannot_target() {
    when(character.canTarget(other)).thenReturn(false);
    when(characters.iterator()).thenReturn(characterIterator);
    when(characterIterator.hasNext()).thenReturn(true, false);
    when(characterIterator.next()).thenReturn(other);

    assertThat(targets.all().characters()).isEmpty();
  }

  @Test
  public void immediate_target_characters() {
    when(character.canTarget(other)).thenReturn(true);
    when(characters.iterator()).thenReturn(characterIterator);
    when(characterIterator.hasNext()).thenReturn(true, false);
    when(characterIterator.next()).thenReturn(other);
    when(other.getCoordinate()).thenReturn(ORIGIN_TARGET);

    assertThat(targets.immediate().characters()).containsExactly(other);
  }

  @Test
  public void immediate_target_characters_cannot_target() {
    when(character.canTarget(other)).thenReturn(false);
    when(characters.iterator()).thenReturn(characterIterator);
    when(characterIterator.hasNext()).thenReturn(true, false);
    when(characterIterator.next()).thenReturn(other);

    assertThat(targets.immediate().characters()).isEmpty();
  }

  @Test
  public void move_terrains() {
    when(terrains.getAll(moveMap.keySet())).thenReturn(terrainIterable);

    assertThat(targets.moveTerrains()).isSameAs(terrainIterable);
  }

  @Test
  public void all_terrains() {
    when(terrains.getAll(targets.all().coordinates())).thenReturn(terrainIterable);

    assertThat(targets.all().terrains()).isSameAs(terrainIterable);
  }

  @Test
  public void immediate_terrains() {
    when(terrains.getAll(targets.immediate().coordinates())).thenReturn(terrainIterable);

    assertThat(targets.immediate().terrains()).isSameAs(terrainIterable);
  }

  /**
   * ORIGIN can hit ORIGIN_TARGET with originWeapon
   * MOVE1 can hit TARGET1 & TARGET2 with weapon1
   * MOVE2 can hit TARGET2 with weapon 1 & 2 and MOVE1 with weapon1
   */
  private Map<Coordinate, SetMultimap<Coordinate, Weapon>> createMoveMap() {
    Map<Coordinate, SetMultimap<Coordinate, Weapon>> moveMap =
        new HashMap<Coordinate, SetMultimap<Coordinate, Weapon>>();
    SetMultimap<Coordinate, Weapon> origin = HashMultimap.create();
    origin.put(ORIGIN_TARGET, originWeapon);
    moveMap.put(ORIGIN, origin);
    SetMultimap<Coordinate, Weapon> move1 = HashMultimap.create();
    move1.put(TARGET1, weapon1);
    move1.put(TARGET2, weapon1);
    moveMap.put(MOVE1, move1);
    SetMultimap<Coordinate, Weapon> move2 = HashMultimap.create();
    move2.put(TARGET2, weapon1);
    move2.put(TARGET2, weapon2);
    move1.put(MOVE1, weapon1);
    moveMap.put(MOVE2, move2);
    return moveMap;
  }
}