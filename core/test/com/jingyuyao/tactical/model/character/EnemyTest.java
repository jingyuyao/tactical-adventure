package com.jingyuyao.tactical.model.character;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.ListenableFuture;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.character.event.InstantMove;
import com.jingyuyao.tactical.model.character.event.Move;
import com.jingyuyao.tactical.model.character.event.RemoveCharacter;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.map.Path;
import com.jingyuyao.tactical.model.map.Targets;
import com.jingyuyao.tactical.model.map.Targets.FilteredTargets;
import com.jingyuyao.tactical.model.map.TargetsFactory;
import com.jingyuyao.tactical.model.map.Terrain;
import com.jingyuyao.tactical.model.mark.Marker;
import com.jingyuyao.tactical.model.mark.Marking;
import com.jingyuyao.tactical.model.mark.MarkingFactory;
import com.jingyuyao.tactical.model.retaliation.Retaliation;
import com.jingyuyao.tactical.model.state.MapState;
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
public class EnemyTest {

  private static final Coordinate COORDINATE = new Coordinate(0, 0);
  private static final Coordinate COORDINATE2 = new Coordinate(0, 1);
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
  private FilteredTargets allTargets;
  @Mock
  private Marking marking;
  @Mock
  private MapState mapState;
  @Mock
  private Move move;
  @Mock
  private InstantMove instantMove;
  @Mock
  private RemoveCharacter removeCharacter;
  @Mock
  private Terrain terrain;
  @Mock
  private Player player;
  @Mock
  private Weapon weapon;
  @Mock
  private Path path;
  @Mock
  private Retaliation retaliation;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;
  @Captor
  private ArgumentCaptor<Map<MapObject, Marker>> markerMapCaptor;

  private List<Marker> markers;
  private List<Terrain> terrainList;
  private Enemy enemy;

  @Before
  public void setUp() {
    markers = new ArrayList<Marker>();
    terrainList = ImmutableList.of(terrain);
    enemy =
        new Enemy(
            eventBus, COORDINATE, markers, NAME, stats, items, targetsFactory, markingFactory,
            retaliation);
    verify(eventBus).register(enemy);
  }

  @Test
  public void dispose() {
    set_up_targets();

    enemy.toggleDangerArea();
    enemy.dispose();

    verify(marking).clear();
    verify(eventBus).unregister(enemy);
  }

  @Test
  public void select() {
    enemy.select(mapState);

    verify(mapState).select(enemy);
  }

  @Test
  public void highlight() {
    enemy.highlight(mapState);

    verify(mapState).highlight(enemy);
  }

  @Test
  public void move() {
    when(path.getDestination()).thenReturn(COORDINATE2);

    ListenableFuture<Void> future = enemy.move(path);

    verify(eventBus).post(argumentCaptor.capture());
    Move move = TestHelpers.isInstanceOf(argumentCaptor.getValue(), Move.class);
    assertThat(move.getPath()).isSameAs(path);
    assertThat(future.isDone()).isFalse();

    move.done();
    assertThat(future.isDone()).isTrue();
  }

  @Test
  public void moved() {
    set_up_targets();
    enemy.toggleDangerArea();

    enemy.moved(move);

    InOrder inOrder = Mockito.inOrder(marking);
    inOrder.verify(marking).apply();
    inOrder.verify(marking).clear();
    inOrder.verify(marking).apply();
  }

  @Test
  public void instant_move() {
    set_up_targets();
    enemy.toggleDangerArea();

    enemy.instantMoved(instantMove);

    InOrder inOrder = Mockito.inOrder(marking);
    inOrder.verify(marking).apply();
    inOrder.verify(marking).clear();
    inOrder.verify(marking).apply();
  }

  @Test
  public void character_removed() {
    set_up_targets();
    enemy.toggleDangerArea();

    enemy.characterRemoved(removeCharacter);

    InOrder inOrder = Mockito.inOrder(marking);
    inOrder.verify(marking).apply();
    inOrder.verify(marking).clear();
    inOrder.verify(marking).apply();
  }

  @Test
  public void name() {
    assertThat(enemy.getName()).isEqualTo(NAME);
  }

  @Test
  public void retaliation() {
    enemy.retaliate();

    verify(retaliation).run(enemy);
  }

  @Test
  public void markers() {
    enemy.addMarker(Marker.DANGER);

    assertThat(markers).containsExactly(Marker.DANGER);

    enemy.removeMarker(Marker.DANGER);

    assertThat(markers).isEmpty();
  }

  @Test
  public void toggle_danger_area() {
    set_up_targets();

    enemy.toggleDangerArea();

    verify(markingFactory).create(eq(enemy), markerMapCaptor.capture());
    assertThat(markerMapCaptor.getValue()).containsExactly(terrain, Marker.DANGER);
  }

  @Test
  public void toggle_danger_area_multiple() {
    set_up_targets();

    enemy.toggleDangerArea();
    enemy.toggleDangerArea();
    enemy.toggleDangerArea();

    InOrder inOrder = Mockito.inOrder(marking);
    inOrder.verify(marking).apply();
    inOrder.verify(marking).clear();
    inOrder.verify(marking).apply();
  }

  @Test
  public void try_hit_no_hp() {
    when(stats.getHp()).thenReturn(0);

    enemy.tryHit(player);

    verifyZeroInteractions(items);
    verifyZeroInteractions(player);
  }

  @Test
  public void try_hit_no_equipped_weapon() {
    when(stats.getHp()).thenReturn(1);
    when(items.getEquippedWeapon()).thenReturn(Optional.<Weapon>absent());

    enemy.tryHit(player);
  }

  @Test
  public void try_hit_cannot_hit() {
    when(stats.getHp()).thenReturn(1);
    when(items.getEquippedWeapon()).thenReturn(Optional.of(weapon));
    when(weapon.canHitFrom(enemy, COORDINATE, player)).thenReturn(false);

    enemy.tryHit(player);

    verify(weapon).canHitFrom(enemy, COORDINATE, player);
    verifyNoMoreInteractions(weapon);
  }

  @Test
  public void try_hit_success() {
    when(stats.getHp()).thenReturn(1);
    when(items.getEquippedWeapon()).thenReturn(Optional.of(weapon));
    when(weapon.canHitFrom(enemy, COORDINATE, player)).thenReturn(true);

    enemy.tryHit(player);

    verify(weapon).canHitFrom(enemy, COORDINATE, player);
    verify(weapon).hit(player);
  }

  @Test
  public void subscribers() {
    TestHelpers.verifyNoDeadEvents(enemy, move, instantMove, removeCharacter);
  }

  private void set_up_targets() {
    when(targetsFactory.create(enemy)).thenReturn(targets);
    when(targets.all()).thenReturn(allTargets);
    when(allTargets.terrains()).thenReturn(terrainList);
    when(markingFactory.create(eq(enemy), Mockito.<Map<MapObject, Marker>>any()))
        .thenReturn(marking);
  }
}