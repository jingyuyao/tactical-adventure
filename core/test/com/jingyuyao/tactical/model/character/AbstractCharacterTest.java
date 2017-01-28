package com.jingyuyao.tactical.model.character;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
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
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.map.Marker;
import com.jingyuyao.tactical.model.map.Path;
import com.jingyuyao.tactical.model.map.TerrainGraphs;
import com.jingyuyao.tactical.model.state.MapState;
import com.jingyuyao.tactical.model.terrain.Terrain;
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
public class AbstractCharacterTest {

  private static final Coordinate CHARACTER_COORDINATE = new Coordinate(100, 100);
  private static final Coordinate DESTINATION = new Coordinate(50, 50);
  private static final Coordinate BLOCKED_COORDINATE = new Coordinate(12, 12);

  @Mock
  private Multiset<Marker> markers;
  @Mock
  private CharacterData data;
  @Mock
  private EventBus eventBus;
  @Mock
  private TerrainGraphs terrainGraphs;
  @Mock
  private Characters characters;
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
  private Terrain cannotPassTerrain;
  @Mock
  private Terrain blockedTerrain;
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
        new CharacterImpl(data, markers, terrainGraphs, characters, eventBus, items);
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
  public void damage_by_not_dead() {
    when(data.isDead()).thenReturn(false);

    character.damageBy(5);

    verify(data).damageBy(5);
    verifyZeroInteractions(characters);
    verifyZeroInteractions(eventBus);
  }

  @Test
  public void damage_by_dead() {
    when(data.isDead()).thenReturn(true);

    character.damageBy(5);

    verify(data).damageBy(5);
    verify(characters).remove(character);
    verify(eventBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isInstanceOf(RemoveSelf.class);
  }

  @Test
  public void heal_by() {
    character.healBy(10);

    verify(data).healBy(10);
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

    verify(data).setCoordinate(DESTINATION);
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

    verify(data).setCoordinate(DESTINATION);
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
    when(data.getCoordinate()).thenReturn(CHARACTER_COORDINATE);
    when(data.getMoveDistance()).thenReturn(10);
    when(characters.coordinates()).thenReturn(ImmutableList.of(BLOCKED_COORDINATE));
    when(terrain.getCoordinate()).thenReturn(DESTINATION);
    when(terrain.canHold(character)).thenReturn(true);
    when(terrain.getMovementPenalty()).thenReturn(123);
    when(blockedTerrain.getCoordinate()).thenReturn(BLOCKED_COORDINATE);
    when(cannotPassTerrain.getCoordinate()).thenReturn(DESTINATION);
    when(cannotPassTerrain.canHold(character)).thenReturn(false);
    when(
        terrainGraphs.distanceFrom(
            Mockito.<Coordinate>any(), Mockito.anyInt(), Mockito.<Function<Terrain, Integer>>any()
        )).thenReturn(coordinateGraph);

    assertThat(character.createMoveGraph()).isSameAs(coordinateGraph);

    verify(terrainGraphs)
        .distanceFrom(
            Mockito.eq(CHARACTER_COORDINATE), Mockito.eq(10), functionCaptor.capture());
    Function<Terrain, Integer> function = functionCaptor.getValue();
    assertThat(function.apply(terrain)).isEqualTo(123);
    assertThat(function.apply(blockedTerrain)).isEqualTo(TerrainGraphs.BLOCKED);
    assertThat(function.apply(cannotPassTerrain)).isEqualTo(TerrainGraphs.BLOCKED);
  }

  private static class CharacterImpl extends AbstractCharacter<CharacterData> {

    CharacterImpl(
        CharacterData data,
        Multiset<Marker> markers,
        TerrainGraphs terrainGraphs,
        Characters characters,
        EventBus eventBus,
        List<Item> items) {
      super(data, markers, items, eventBus, terrainGraphs, characters);
    }

    @Override
    public void select(MapState mapState) {

    }
  }
}