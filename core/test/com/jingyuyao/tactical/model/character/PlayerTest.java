package com.jingyuyao.tactical.model.character;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.ListenableFuture;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.character.event.Move;
import com.jingyuyao.tactical.model.character.event.NewActionState;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.map.Path;
import com.jingyuyao.tactical.model.map.Targets;
import com.jingyuyao.tactical.model.map.TargetsFactory;
import com.jingyuyao.tactical.model.map.Terrain;
import com.jingyuyao.tactical.model.mark.Marker;
import com.jingyuyao.tactical.model.state.MapState;
import com.jingyuyao.tactical.model.state.Waiting.EndTurn;
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
public class PlayerTest {

  private static final Coordinate COORDINATE = new Coordinate(0, 0);
  private static final Coordinate COORDINATE2 = new Coordinate(1, 0);
  private static final String NAME = "me";

  @Mock
  private EventBus eventBus;
  @Mock
  private Stats stats;
  @Mock
  private Items items;
  @Mock
  private TargetsFactory targetsFactory;
  @Mock
  private Targets targets;
  @Mock
  private MapState mapState;
  @Mock
  private EndTurn endTurn;
  @Mock
  private Terrain terrain;
  @Mock
  private Path path;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  private List<Terrain> terrainList;
  private List<Marker> markers;
  private Player player;

  @Before
  public void setUp() {
    markers = new ArrayList<Marker>();
    terrainList = ImmutableList.of(terrain);
    player =
        new Player(
            eventBus, COORDINATE, markers, NAME, stats, items, targetsFactory);
    verify(eventBus).register(player);
    assertThat(player.isActionable()).isTrue();
  }

  @Test
  public void dispose() {
    show_moves();

    player.dispose();

    verify(eventBus).unregister(player);
    verify(terrain).removeMarker(Marker.CAN_MOVE_TO);
  }

  @Test
  public void select() {
    player.select(mapState);

    verify(mapState).select(player);
  }

  @Test
  public void highlight() {
    player.highlight(mapState);

    verify(mapState).highlight(player);
  }

  @Test
  public void move() {
    when(path.getDestination()).thenReturn(COORDINATE2);

    ListenableFuture<Void> future = player.move(path);

    verify(eventBus).post(argumentCaptor.capture());
    Move move = TestHelpers.isInstanceOf(argumentCaptor.getValue(), Move.class);
    assertThat(move.getPath()).isSameAs(path);
    assertThat(future.isDone()).isFalse();

    move.done();
    assertThat(future.isDone()).isTrue();
  }

  @Test
  public void end_turn() {
    player.setActionable(false);

    player.endTurn(endTurn);

    assertThat(player.isActionable()).isTrue();
  }

  @Test
  public void name() {
    assertThat(player.getName()).isEqualTo(NAME);
  }

  @Test
  public void markers() {
    player.addMarker(Marker.DANGER);

    assertThat(markers).containsExactly(Marker.DANGER);

    player.removeMarker(Marker.DANGER);

    assertThat(markers).isEmpty();
  }

  @Test
  public void set_actionable() {
    player.setActionable(false);

    verify(eventBus).post(argumentCaptor.capture());
    NewActionState newActionState =
        TestHelpers.isInstanceOf(argumentCaptor.getValue(), NewActionState.class);
    assertThat(newActionState.getObject()).isSameAs(player);
    assertThat(newActionState.isActionable()).isFalse();
  }

  @Test
  public void show_moves() {
    when(targetsFactory.create(player)).thenReturn(targets);
    when(targets.moveTerrains()).thenReturn(terrainList);

    player.showMoves();

    verify(terrain).addMarker(Marker.CAN_MOVE_TO);
  }

  @Test
  public void clear_marking() {
    show_moves();
    player.clearMarking();

    verify(terrain).removeMarker(Marker.CAN_MOVE_TO);
  }

  @Test
  public void subscribers() {
    TestHelpers.verifyNoDeadEvents(player, endTurn);
  }
}