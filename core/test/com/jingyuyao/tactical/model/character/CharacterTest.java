package com.jingyuyao.tactical.model.character;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;
import com.google.common.eventbus.EventBus;
import com.google.common.graph.ValueGraph;
import com.google.common.util.concurrent.ListenableFuture;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.character.event.Attack;
import com.jingyuyao.tactical.model.character.event.InstantMove;
import com.jingyuyao.tactical.model.character.event.Move;
import com.jingyuyao.tactical.model.character.event.RemoveSelf;
import com.jingyuyao.tactical.model.common.Algorithms;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.map.Marker;
import com.jingyuyao.tactical.model.map.Path;
import com.jingyuyao.tactical.model.map.Terrain;
import com.jingyuyao.tactical.model.map.Terrain.Type;
import com.jingyuyao.tactical.model.state.MapState;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CharacterTest {

  private static final Coordinate CHARACTER_COORDINATE = new Coordinate(100, 100);
  private static final Coordinate DESTINATION = new Coordinate(50, 50);
  private static final Coordinate BLOCKED_COORDINATE = new Coordinate(12, 12);
  private static final String NAME = "hello";

  @Mock
  private Multiset<Marker> markers;
  @Mock
  private Algorithms algorithms;
  @Mock
  private Characters characters;
  @Mock
  private EventBus eventBus;
  @Mock
  private Stats stats;
  @Mock
  private Weapon weapon1;
  @Mock
  private Weapon weapon2;
  @Mock
  private Consumable consumable;
  @Mock
  private Item newItem;
  @Mock
  private MapState mapState;
  @Mock
  private Path path;
  @Mock
  private Target target;
  @Mock
  private Object listener;
  @Mock
  private Terrain terrain;
  @Mock
  private ValueGraph<Coordinate, Integer> coordinateGraph;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;
  @Captor
  private ArgumentCaptor<Function<Terrain, Integer>> functionCaptor;

  private List<Item> items;
  private Character character;

  @Before
  public void setUp() {
    items = Lists.newArrayList(weapon1, consumable, weapon2);
    character =
        new CharacterImpl(
            CHARACTER_COORDINATE, markers, algorithms, characters, eventBus, stats, items);
  }

  @Test
  public void highlight() {
    character.highlight(mapState);

    verify(mapState).highlight(character);
  }

  @Test
  public void register_listener() {
    character.registerListener(listener);

    verify(eventBus).register(listener);
  }

  @Test
  public void name() {
    when(stats.getName()).thenReturn(NAME);

    assertThat(character.getName()).isEqualTo(NAME);
  }

  @Test
  public void hp() {
    when(stats.getHp()).thenReturn(1);

    assertThat(character.getHp()).isEqualTo(1);
  }

  @Test
  public void can_pass_terrain_type() {
    when(stats.canPassTerrainType(Type.NORMAL)).thenReturn(true);
    when(stats.canPassTerrainType(Type.MOUNTAIN)).thenReturn(false);

    assertThat(character.canPassTerrainType(Type.NORMAL)).isTrue();
    assertThat(character.canPassTerrainType(Type.MOUNTAIN)).isFalse();
  }

  @Test
  public void damage_by_not_dead() {
    when(stats.isDead()).thenReturn(false);

    character.damageBy(5);

    verify(stats).damageBy(5);
    verifyZeroInteractions(characters);
    verifyZeroInteractions(eventBus);
  }

  @Test
  public void damage_by_dead() {
    when(stats.isDead()).thenReturn(true);

    character.damageBy(5);

    verify(stats).damageBy(5);
    verify(characters).removeCharacter(character);
    verify(eventBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isInstanceOf(RemoveSelf.class);
  }

  @Test
  public void heal_by() {
    character.healBy(10);

    verify(stats).healBy(10);
  }

  @Test
  public void add_item() {
    character.addItem(newItem);

    assertThat(character.getItems()).contains(newItem);
  }

  @Test
  public void remove_item() {
    character.removeItem(weapon1);

    assertThat(items).containsExactly(consumable, weapon2).inOrder();
  }

  @Test
  public void get_items() {
    assertThat(character.getItems()).containsExactly(weapon1, consumable, weapon2).inOrder();
    assertThat(character.getWeapons()).containsExactly(weapon1, weapon2).inOrder();
    assertThat(character.getConsumables()).containsExactly(consumable);
  }

  @Test
  public void quick_access() {
    character.quickAccess(weapon2);

    assertThat(items).containsExactly(weapon2, consumable, weapon1).inOrder();
  }

  @Test
  public void move_along() {
    when(path.getDestination()).thenReturn(DESTINATION);

    ListenableFuture<Void> future = character.moveAlong(path);

    assertThat(character.getCoordinate()).isEqualTo(DESTINATION);
    verify(eventBus).post(argumentCaptor.capture());
    Move move = TestHelpers.isInstanceOf(argumentCaptor.getValue(), Move.class);
    assertThat(move.getPath()).isSameAs(path);
    assertThat(future.isDone()).isFalse();

    move.done();
    assertThat(future.isDone()).isTrue();
  }

  @Test
  public void instant_move() {
    character.instantMoveTo(DESTINATION);

    assertThat(character.getCoordinate()).isEqualTo(DESTINATION);
    verify(eventBus).post(argumentCaptor.capture());
    InstantMove instantMove =
        TestHelpers.isInstanceOf(argumentCaptor.getValue(), InstantMove.class);
    assertThat(instantMove.getObject()).isSameAs(character);
    assertThat(instantMove.getDestination()).isEqualTo(DESTINATION);
  }

  @Test
  public void attacks() {
    ListenableFuture<Void> future = character.attacks(target);

    verify(eventBus).post(argumentCaptor.capture());
    Attack attack = TestHelpers.isInstanceOf(argumentCaptor.getValue(), Attack.class);
    assertThat(attack.getObject()).isSameAs(target);
    assertThat(future.isDone()).isFalse();

    attack.done();
    assertThat(future.isDone()).isTrue();
  }

  @Test
  public void create_move_graph() {
    when(stats.getMoveDistance()).thenReturn(10);
    when(characters.coordinates()).thenReturn(ImmutableSet.of(BLOCKED_COORDINATE));
    when(terrain.getCoordinate()).thenReturn(BLOCKED_COORDINATE, DESTINATION);
    when(terrain.getMovementPenalty(character)).thenReturn(123);
    when(
        algorithms.distanceFromGraph(
            Mockito.<Function<Terrain, Integer>>any(),
            Mockito.<Coordinate>any(),
            Mockito.anyInt())).thenReturn(coordinateGraph);

    assertThat(character.createMoveGraph()).isSameAs(coordinateGraph);

    verify(algorithms)
        .distanceFromGraph(
            functionCaptor.capture(), Mockito.eq(CHARACTER_COORDINATE), Mockito.eq(10));
    Function<Terrain, Integer> function = functionCaptor.getValue();
    assertThat(function.apply(terrain)).isEqualTo(Algorithms.NO_EDGE);
    assertThat(function.apply(terrain)).isEqualTo(123);
  }

  private static class CharacterImpl extends Character {

    CharacterImpl(
        Coordinate coordinate,
        Multiset<Marker> markers,
        Algorithms algorithms,
        Characters characters,
        EventBus eventBus,
        Stats stats,
        List<Item> items) {
      super(coordinate, markers, algorithms, characters, eventBus, stats, items);
    }

    @Override
    public void select(MapState mapState) {

    }
  }
}