package com.jingyuyao.tactical.model.map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

import com.google.common.collect.ImmutableMultimap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MarkingTest {

  @Mock
  private MapObject object;

  private ImmutableMultimap<MapObject, Marker> markerMap;
  private Marking marking;

  @Before
  public void setUp() {
    markerMap = ImmutableMultimap.of(object, Marker.CAN_ATTACK);
    marking = new Marking(markerMap);
  }

  @Test
  public void apply() {
    marking.apply();

    verify(object).addMarker(Marker.CAN_ATTACK);
    verifyNoMoreInteractions(object);
  }

  @Test
  public void apply_twice() {
    marking.apply();
    marking.apply();

    verify(object).addMarker(Marker.CAN_ATTACK);
    verifyNoMoreInteractions(object);
  }

  @Test
  public void clear_apply() {
    marking.clear();
    marking.apply();

    verify(object).addMarker(Marker.CAN_ATTACK);
    verifyNoMoreInteractions(object);
  }

  @Test
  public void apply_clear() {
    marking.apply();
    marking.clear();

    InOrder inOrder = Mockito.inOrder(object);
    inOrder.verify(object).addMarker(Marker.CAN_ATTACK);
    inOrder.verify(object).removeMarker(Marker.CAN_ATTACK);
    verifyNoMoreInteractions(object);
  }

  @Test
  public void clear() {
    marking.clear();

    verifyZeroInteractions(object);
  }

  @Test
  public void clear_twice() {
    marking.clear();
    marking.clear();

    verifyZeroInteractions(object);
  }
}