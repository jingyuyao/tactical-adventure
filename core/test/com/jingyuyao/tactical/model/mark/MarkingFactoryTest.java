package com.jingyuyao.tactical.model.mark;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.common.Waiter;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.map.Targets;
import com.jingyuyao.tactical.model.map.Targets.FilteredTargets;
import com.jingyuyao.tactical.model.map.Terrain;
import java.util.Map;
import javax.inject.Provider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MarkingFactoryTest {

  private static final Coordinate COORDINATE1 = new Coordinate(0, 0);
  private static final Coordinate COORDINATE2 = new Coordinate(0, 1);

  @Mock
  private EventBus eventBus;
  @Mock
  private Waiter waiter;
  @Mock
  private Provider<Map<MapObject, Marker>> markerMapProvider;
  @Mock
  private Map<MapObject, Marker> markerMap;
  @Mock
  private Targets targets;
  @Mock
  private Terrain terrain;
  @Mock
  private Terrain terrain2;
  @Mock
  private Character character;
  @Mock
  private Character character2;
  @Mock
  private FilteredTargets allTargets;
  @Mock
  private FilteredTargets immediateTargets;

  private ImmutableSet<Coordinate> coordinates;
  private ImmutableSet<Coordinate> coordinates2;
  private Iterable<Terrain> terrainIterable;
  private Iterable<Terrain> terrainIterable2;
  private ImmutableList<Character> characterList;

  private MarkingFactory markingFactory;

  @Before
  public void setUp() {
    coordinates = ImmutableSet.of(COORDINATE1);
    coordinates2 = ImmutableSet.of(COORDINATE2);
    terrainIterable = ImmutableList.of(terrain);
    terrainIterable2 = ImmutableList.of(terrain2);
    characterList = ImmutableList.of(character, character2);

    markingFactory = new MarkingFactory(eventBus, waiter, markerMapProvider);
  }

  @Test
  public void all_targets_with_move() {
    when(markerMapProvider.get()).thenReturn(markerMap);
    when(targets.moveTerrains()).thenReturn(terrainIterable);
    when(targets.all()).thenReturn(allTargets);
    when(allTargets.terrains()).thenReturn(terrainIterable2);
    when(allTargets.characters()).thenReturn(characterList);

    markingFactory.allTargetsWithMove(targets);

    verifyMarkers(Marker.CAN_MOVE_TO, terrain);
    verifyMarkers(Marker.CAN_ATTACK, terrain2);
    verifyMarkers(Marker.POTENTIAL_TARGET, character);
    verifyMarkers(Marker.POTENTIAL_TARGET, character2);
    verifyNoMoreInteractions(markerMap);
  }

  @Test
  public void immediateTargets() {
    when(markerMapProvider.get()).thenReturn(markerMap);
    when(targets.immediate()).thenReturn(immediateTargets);
    when(immediateTargets.terrains()).thenReturn(terrainIterable);
    when(immediateTargets.characters()).thenReturn(characterList);

    markingFactory.immediateTargets(targets);

    verifyMarkers(Marker.CAN_ATTACK, terrain);
    verifyMarkers(Marker.POTENTIAL_TARGET, character);
    verifyMarkers(Marker.POTENTIAL_TARGET, character2);
    verifyNoMoreInteractions(markerMap);
  }

  @Test
  public void immediate_targets_with_chosen_target() {
    when(markerMapProvider.get()).thenReturn(markerMap);
    when(targets.immediate()).thenReturn(immediateTargets);
    when(immediateTargets.terrains()).thenReturn(terrainIterable);

    markingFactory.immediateTargetsWithChosenCharacter(targets, character2);

    verifyMarkers(Marker.CAN_ATTACK, terrain);
    verifyMarkers(Marker.CHOSEN_TARGET, character2);
    verifyNoMoreInteractions(markerMap);
  }

  private void verifyMarkers(Marker expected, MapObject... objects) {
    for (MapObject object : objects) {
      verify(markerMap).put(object, expected);
    }
  }
}