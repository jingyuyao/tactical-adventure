package com.jingyuyao.tactical.model.map;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.Algorithms;
import com.jingyuyao.tactical.model.Coordinate;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.map.Terrain.Type;
import com.jingyuyao.tactical.model.map.event.SyncMarkers;
import com.jingyuyao.tactical.model.mark.Marker;
import com.jingyuyao.tactical.model.state.MapState;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TerrainTest {

  private static final Coordinate COORDINATE = new Coordinate(10, 10);
  private static final Type TYPE = Type.NORMAL;

  @Mock
  private EventBus eventBus;
  @Mock
  private MapState mapState;
  @Mock
  private Character character;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  // copied as an immutable list so we need a real instance
  private List<Marker> markers;
  private Terrain terrain;

  @Before
  public void setUp() {
    markers = new ArrayList<Marker>();
    terrain = new Terrain(eventBus, COORDINATE, markers, TYPE);
  }

  @Test
  public void select() {
    terrain.select(mapState);

    verify(mapState).select(terrain);
  }

  @Test
  public void highlight() {
    terrain.highlight(mapState);

    verify(mapState).highlight(terrain);
  }

  @Test
  public void getType() {
    assertThat(terrain.getType()).isEqualTo(TYPE);
  }

  @Test
  public void move_penalty_can_pass() {
    when(character.canPassTerrainType(TYPE)).thenReturn(true);

    // we are not going to test the actual values for not
    assertThat(terrain.getMovementPenalty(character)).isNotEqualTo(Algorithms.NO_EDGE);
  }

  @Test
  public void move_penalty_cannot_pass() {
    when(character.canPassTerrainType(TYPE)).thenReturn(false);

    assertThat(terrain.getMovementPenalty(character)).isEqualTo(Algorithms.NO_EDGE);
  }

  @Test
  public void markers() {
    terrain.addMarker(Marker.CAN_ATTACK);
    assertThat(markers).contains(Marker.CAN_ATTACK);

    terrain.removeMarker(Marker.CAN_ATTACK);
    assertThat(markers).isEmpty();

    verify(eventBus, times(2)).post(argumentCaptor.capture());
    SyncMarkers syncMarkers = TestHelpers
        .isInstanceOf(argumentCaptor.getAllValues().get(0), SyncMarkers.class);
    assertThat(syncMarkers.getObject()).isSameAs(terrain);
    assertThat(syncMarkers.getMarkers()).contains(Marker.CAN_ATTACK);

    SyncMarkers syncMarkers2 = TestHelpers
        .isInstanceOf(argumentCaptor.getAllValues().get(1), SyncMarkers.class);
    assertThat(syncMarkers2.getObject()).isSameAs(terrain);
    assertThat(syncMarkers2.getMarkers()).isEmpty();
  }
}