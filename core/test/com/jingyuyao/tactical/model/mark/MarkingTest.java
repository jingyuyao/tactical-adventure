package com.jingyuyao.tactical.model.mark;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.jingyuyao.tactical.model.map.MapObject;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MarkingTest {

  private static final Marker DUMMY_MARKER = Marker.DANGER;

  @Mock
  private Map<MapObject, Marker> markerMap;
  @Mock
  private Set<Entry<MapObject, Marker>> entrySet;
  @Mock
  private Iterator<Entry<MapObject, Marker>> iterator;
  @Mock
  private Entry<MapObject, Marker> entry;
  @Mock
  private MapObject mapObject;

  private Marking marking;

  @Before
  public void setUp() {
    marking = new Marking(markerMap);
  }

  @Test
  public void apply() {
    set_up_marker_map();

    marking.apply();

    verify(mapObject).addMarker(DUMMY_MARKER);
  }

  @Test
  public void apply_twice() {
    set_up_marker_map();

    marking.apply();
    marking.apply();

    verify(mapObject).addMarker(DUMMY_MARKER);
  }

  @Test
  public void clear_apply() {
    set_up_marker_map();

    marking.clear();
    marking.apply();

    verify(mapObject).addMarker(DUMMY_MARKER);
  }

  @Test
  public void apply_clear() {
    set_up_marker_map();

    marking.apply();
    marking.clear();

    InOrder inOrder = Mockito.inOrder(mapObject);
    inOrder.verify(mapObject).addMarker(DUMMY_MARKER);
    inOrder.verify(mapObject).removeMarker(DUMMY_MARKER);
  }

  @Test
  public void clear() {
    set_up_marker_map();

    marking.clear();

    verifyZeroInteractions(mapObject);
  }

  @Test
  public void clear_twice() {
    set_up_marker_map();

    marking.clear();
    marking.clear();

    verifyZeroInteractions(mapObject);
  }

  private void set_up_marker_map() {
    when(markerMap.entrySet()).thenReturn(entrySet);
    when(entrySet.iterator()).thenReturn(iterator);
    // marker map is iterated at most twice in the life cycle of a marking
    // once for apply and once for clear
    when(iterator.hasNext()).thenReturn(true, false, true, false);
    when(iterator.next()).thenReturn(entry);
    when(entry.getKey()).thenReturn(mapObject);
    when(entry.getValue()).thenReturn(DUMMY_MARKER);
  }
}