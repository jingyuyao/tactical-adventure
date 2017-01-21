package com.jingyuyao.tactical.model.mark;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.mark.event.HideMarking;
import com.jingyuyao.tactical.model.mark.event.ShowMarking;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MarkingTest {

  @Mock
  private EventBus eventBus;
  @Mock
  private Map<MapObject, Marker> markerMap;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  private Marking marking;

  @Before
  public void setUp() {
    marking = new Marking(eventBus, markerMap);
  }

  @Test
  public void apply() {
    marking.apply();

    verify(eventBus).post(argumentCaptor.capture());
    ShowMarking showMarking =
        TestHelpers.isInstanceOf(argumentCaptor.getValue(), ShowMarking.class);
    assertThat(showMarking.getObject()).isSameAs(markerMap);
  }

  @Test
  public void apply_twice() {
    marking.apply();
    marking.apply();

    verify(eventBus).post(argumentCaptor.capture());
    ShowMarking showMarking =
        TestHelpers.isInstanceOf(argumentCaptor.getValue(), ShowMarking.class);
    assertThat(showMarking.getObject()).isSameAs(markerMap);
  }

  @Test
  public void clear_apply() {
    marking.clear();
    marking.apply();

    verify(eventBus).post(argumentCaptor.capture());
    ShowMarking showMarking =
        TestHelpers.isInstanceOf(argumentCaptor.getValue(), ShowMarking.class);
    assertThat(showMarking.getObject()).isSameAs(markerMap);
  }

  @Test
  public void apply_clear() {
    marking.apply();
    marking.clear();

    verify(eventBus, times(2)).post(argumentCaptor.capture());
    ShowMarking showMarking =
        TestHelpers.isInstanceOf(argumentCaptor.getAllValues().get(0), ShowMarking.class);
    assertThat(showMarking.getObject()).isSameAs(markerMap);
    HideMarking hideMarking =
        TestHelpers.isInstanceOf(argumentCaptor.getAllValues().get(1), HideMarking.class);
    assertThat(hideMarking.getObject()).isSameAs(markerMap);
  }

  @Test
  public void clear() {
    marking.clear();

    verifyZeroInteractions(eventBus);
  }

  @Test
  public void clear_twice() {
    marking.clear();
    marking.clear();

    verifyZeroInteractions(eventBus);
  }
}