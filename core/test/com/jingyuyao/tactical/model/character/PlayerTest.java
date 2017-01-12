package com.jingyuyao.tactical.model.character;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.character.event.NewActionState;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.map.Targets;
import com.jingyuyao.tactical.model.map.Targets.FilteredTargets;
import com.jingyuyao.tactical.model.map.TargetsFactory;
import com.jingyuyao.tactical.model.map.Terrain;
import com.jingyuyao.tactical.model.mark.Marker;
import com.jingyuyao.tactical.model.mark.Marking;
import com.jingyuyao.tactical.model.mark.MarkingFactory;
import com.jingyuyao.tactical.model.state.MapState;
import com.jingyuyao.tactical.model.state.Waiting.EndTurn;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
public class PlayerTest {

  private static final Coordinate COORDINATE = new Coordinate(0, 0);
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
  private MarkingFactory markingFactory;
  @Mock
  private Targets targets;
  @Mock
  private FilteredTargets filteredTargets;
  @Mock
  private Marking marking;
  @Mock
  private MapState mapState;
  @Mock
  private EndTurn endTurn;
  @Mock
  private Enemy enemy;
  @Mock
  private Character character;
  @Mock
  private Terrain terrain;
  @Mock
  private Terrain terrain2;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;
  @Captor
  private ArgumentCaptor<Map<MapObject, Marker>> markerMapCaptor;

  private List<Terrain> terrainList;
  private List<Terrain> terrainList2;
  private List<Character> characterList;
  private List<Marker> markers;
  private Player player;

  @Before
  public void setUp() {
    markers = new ArrayList<Marker>();
    terrainList = ImmutableList.of(terrain);
    terrainList2 = ImmutableList.of(terrain, terrain2);
    characterList = ImmutableList.of(character);
    player =
        new Player(
            eventBus, COORDINATE, markers, NAME, stats, items, targetsFactory, markingFactory);
    verify(eventBus).register(player);
    assertThat(player.isActionable()).isTrue();
  }

  @Test
  public void dispose() {
    show_immediate_targets();

    player.dispose();

    verify(eventBus).unregister(player);
    verify(marking).clear();
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
  public void show_immediate_targets() {
    when(targetsFactory.create(player)).thenReturn(targets);
    when(targets.immediate()).thenReturn(filteredTargets);
    when(filteredTargets.terrains()).thenReturn(terrainList);
    when(filteredTargets.characters()).thenReturn(characterList);
    when(markingFactory.create(eq(player), Mockito.<Map<MapObject, Marker>>any()))
        .thenReturn(marking);

    player.showImmediateTargets();

    verify(marking).apply();
    verify(markingFactory).create(eq(player), markerMapCaptor.capture());
    assertThat(markerMapCaptor.getValue())
        .containsExactly(terrain, Marker.CAN_ATTACK, character, Marker.POTENTIAL_TARGET);
  }

  @Test
  public void show_immediate_targets_with_chosen_enemy() {
    when(targetsFactory.create(player)).thenReturn(targets);
    when(targets.immediate()).thenReturn(filteredTargets);
    when(filteredTargets.terrains()).thenReturn(terrainList);
    when(markingFactory.create(eq(player), Mockito.<Map<MapObject, Marker>>any()))
        .thenReturn(marking);

    player.showImmediateTargetsWithChosenTarget(enemy);

    verify(marking).apply();
    verify(markingFactory).create(eq(player), markerMapCaptor.capture());
    assertThat(markerMapCaptor.getValue())
        .containsExactly(terrain, Marker.CAN_ATTACK, enemy, Marker.CHOSEN_TARGET);
  }

  @Test
  public void show_move_and_targets() {
    when(targetsFactory.create(player)).thenReturn(targets);
    when(targets.moveTerrains()).thenReturn(terrainList);
    when(targets.all()).thenReturn(filteredTargets);
    when(filteredTargets.terrains()).thenReturn(terrainList2);
    when(filteredTargets.characters()).thenReturn(characterList);
    when(markingFactory.create(eq(player), Mockito.<Map<MapObject, Marker>>any()))
        .thenReturn(marking);

    player.showAllTargetsWithMove();

    verify(marking).apply();
    verify(markingFactory).create(eq(player), markerMapCaptor.capture());
    assertThat(markerMapCaptor.getValue())
        .containsExactly(
            terrain, Marker.CAN_MOVE_TO,
            terrain2, Marker.CAN_ATTACK,
            character, Marker.POTENTIAL_TARGET);
  }

  @Test
  public void clear_marking() {
    show_move_and_targets();
    player.clearMarking();

    InOrder inOrder = Mockito.inOrder(marking);
    inOrder.verify(marking).apply();
    inOrder.verify(marking).clear();
  }

  @Test
  public void subscribers() {
    TestHelpers.verifyNoDeadEvents(player, endTurn);
  }
}