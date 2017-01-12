package com.jingyuyao.tactical.model.mark;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.event.RemoveCharacter;
import com.jingyuyao.tactical.model.common.Waiter;
import com.jingyuyao.tactical.model.map.MapObject;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MarkingTest {

  private static final Marker DUMMY_MARKER = Marker.DANGER;

  @Mock
  private EventBus eventBus;
  @Mock
  private Character character;
  @Mock
  private Map<MapObject, Marker> markerMap;
  @Mock
  private Waiter waiter;
  @Mock
  private RemoveCharacter removeCharacter;
  @Mock
  private Set<Entry<MapObject, Marker>> entrySet;
  @Mock
  private Iterator<Entry<MapObject, Marker>> iterator;
  @Mock
  private Entry<MapObject, Marker> entry;
  @Mock
  private MapObject mapObject;
  @Captor
  private ArgumentCaptor<Runnable> argumentCaptor;

  private Marking marking;

  @Before
  public void setUp() {
    marking = new Marking(eventBus, waiter, character, markerMap);
    verify(eventBus).register(marking);
  }

  @Test
  public void remove_character_matches_applied() {
    set_up_marker_map();
    when(removeCharacter.matches(character)).thenReturn(true);

    marking.apply();
    marking.removeCharacter(removeCharacter);

    verify(waiter, times(2)).runOnce(argumentCaptor.capture());
    runRunnables(argumentCaptor.getAllValues());

    InOrder inOrder = Mockito.inOrder(eventBus, mapObject);
    inOrder.verify(mapObject).addMarker(DUMMY_MARKER);
    inOrder.verify(eventBus).unregister(marking);
    inOrder.verify(mapObject).removeMarker(DUMMY_MARKER);
  }

  @Test
  public void remove_character_matches_not_applied() {
    set_up_marker_map();
    when(removeCharacter.matches(character)).thenReturn(true);

    marking.removeCharacter(removeCharacter);

    verify(waiter).runOnce(argumentCaptor.capture());
    argumentCaptor.getValue().run();

    verifyZeroInteractions(mapObject);
    verify(eventBus).unregister(marking);
  }

  @Test
  public void remove_character_not_matches() {
    when(removeCharacter.matches(character)).thenReturn(false);

    marking.removeCharacter(removeCharacter);

    verifyZeroInteractions(waiter);
    verifyZeroInteractions(eventBus);
    verifyZeroInteractions(mapObject);
  }

  @Test
  public void apply() {
    set_up_marker_map();

    marking.apply();

    verify(waiter).runOnce(argumentCaptor.capture());
    argumentCaptor.getValue().run();

    verify(mapObject).addMarker(DUMMY_MARKER);
  }

  @Test
  public void apply_twice() {
    set_up_marker_map();

    marking.apply();
    marking.apply();

    verify(waiter, times(2)).runOnce(argumentCaptor.capture());
    runRunnables(argumentCaptor.getAllValues());

    verify(mapObject).addMarker(DUMMY_MARKER);
  }

  @Test
  public void clear_apply() {
    set_up_marker_map();

    marking.clear();
    marking.apply();

    verify(waiter, times(2)).runOnce(argumentCaptor.capture());
    runRunnables(argumentCaptor.getAllValues());

    verifyZeroInteractions(mapObject);
    verify(eventBus).unregister(marking);
  }

  @Test
  public void apply_clear() {
    set_up_marker_map();

    marking.apply();
    marking.clear();

    verify(waiter, times(2)).runOnce(argumentCaptor.capture());
    runRunnables(argumentCaptor.getAllValues());

    InOrder inOrder = Mockito.inOrder(mapObject, eventBus);
    inOrder.verify(mapObject).addMarker(DUMMY_MARKER);
    inOrder.verify(eventBus).unregister(marking);
    inOrder.verify(mapObject).removeMarker(DUMMY_MARKER);
  }

  @Test
  public void clear() {
    set_up_marker_map();

    marking.clear();

    verify(waiter).runOnce(argumentCaptor.capture());
    argumentCaptor.getValue().run();

    verifyZeroInteractions(mapObject);
    verify(eventBus).unregister(marking);
  }

  @Test
  public void clear_twice() {
    set_up_marker_map();

    marking.clear();
    marking.clear();

    verify(waiter, times(2)).runOnce(argumentCaptor.capture());
    runRunnables(argumentCaptor.getAllValues());

    verify(eventBus).unregister(marking);
    verifyZeroInteractions(mapObject);
  }

  @Test
  public void subscribers() {
    TestHelpers.verifyNoDeadEvents(marking, removeCharacter);
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

  private void runRunnables(Iterable<Runnable> runnables) {
    for (Runnable runnable : runnables) {
      runnable.run();
    }
  }
}