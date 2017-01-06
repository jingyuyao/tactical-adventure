package com.jingyuyao.tactical.model.mark;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.Coordinate;
import com.jingyuyao.tactical.model.Waiter;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.map.Targets;
import com.jingyuyao.tactical.model.map.Terrain;
import com.jingyuyao.tactical.model.map.Terrains;
import java.util.Iterator;
import java.util.Map;
import javax.inject.Provider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MarkingFactoryTest {

  @Mock
  private EventBus eventBus;
  @Mock
  private Waiter waiter;
  @Mock
  private Terrains terrains;
  @Mock
  private Provider<Map<MapObject, Marker>> markerMapProvider;
  @Mock
  private Map<MapObject, Marker> markerMap;
  @Mock
  private Targets targets;
  @Mock
  private ImmutableSet<Coordinate> coordinates;
  @Mock
  private ImmutableSet<Coordinate> coordinates2;
  @Mock
  private Iterable<Terrain> terrainIterable;
  @Mock
  private Iterable<Terrain> terrainIterable2;
  @Mock
  private Iterator<Terrain> terrainIterator;
  @Mock
  private Iterator<Terrain> terrainIterator2;
  @Mock
  private Terrain terrain;
  @Mock
  private Terrain terrain2;
  @Mock
  private ImmutableList<Character> characterList;
  @Mock
  private UnmodifiableIterator<Character> characterIterator;
  @Mock
  private Character character;

  private MarkingFactory markingFactory;

  @Before
  public void setUp() {
    markingFactory = new MarkingFactory(eventBus, waiter, terrains, markerMapProvider);
  }

  @Test
  public void moveAndTargets() {
    when(markerMapProvider.get()).thenReturn(markerMap);
    when(targets.moves()).thenReturn(coordinates);
    when(targets.allTargetsMinusMove()).thenReturn(coordinates2);
    when(targets.allTargetCharacters()).thenReturn(characterList);
    set_up_terrain_mocks();
    set_up_character_mocks();

    markingFactory.moveAndTargets(targets);

    verifyMarkers(Marker.CAN_MOVE_TO, terrain);
    verifyMarkers(Marker.CAN_ATTACK, terrain2);
    verifyMarkers(Marker.POTENTIAL_TARGET, character);
    verifyNoMoreInteractions(markerMap);
  }

  @Test
  public void immediateTargets() {
    when(markerMapProvider.get()).thenReturn(markerMap);
    when(targets.immediateTargets()).thenReturn(coordinates);
    when(targets.immediateTargetCharacters()).thenReturn(characterList);
    set_up_terrain_mocks();
    set_up_character_mocks();

    markingFactory.immediateTargets(targets);

    verifyMarkers(Marker.CAN_ATTACK, terrain);
    verifyMarkers(Marker.POTENTIAL_TARGET, character);
    verifyNoMoreInteractions(markerMap);
  }

  @Test
  public void dangerArea() {
    when(markerMapProvider.get()).thenReturn(markerMap);
    when(targets.allTargets()).thenReturn(coordinates);
    set_up_terrain_mocks();

    markingFactory.danger(targets);

    verifyMarkers(Marker.DANGER, terrain);
    verifyNoMoreInteractions(markerMap);
  }

  private void set_up_terrain_mocks() {
    when(terrains.getAll(coordinates)).thenReturn(terrainIterable);
    when(terrainIterable.iterator()).thenReturn(terrainIterator);
    when(terrainIterator.hasNext()).thenReturn(true, false);
    when(terrainIterator.next()).thenReturn(terrain);
    when(terrains.getAll(coordinates2)).thenReturn(terrainIterable2);
    when(terrainIterable2.iterator()).thenReturn(terrainIterator2);
    when(terrainIterator2.hasNext()).thenReturn(true, false);
    when(terrainIterator2.next()).thenReturn(terrain2);
  }

  private void set_up_character_mocks() {
    when(characterList.iterator()).thenReturn(characterIterator);
    when(characterIterator.hasNext()).thenReturn(true, false);
    when(characterIterator.next()).thenReturn(character);
  }

  private void verifyMarkers(Marker expected, MapObject... objects) {
    for (MapObject object : objects) {
      verify(markerMap).put(object, expected);
    }
  }
}