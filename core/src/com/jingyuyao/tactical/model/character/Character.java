package com.jingyuyao.tactical.model.character;

import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multiset;
import com.google.common.eventbus.EventBus;
import com.google.common.graph.Graph;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
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
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.map.Marker;
import com.jingyuyao.tactical.model.map.Path;
import com.jingyuyao.tactical.model.map.Terrain;
import com.jingyuyao.tactical.model.map.TerrainGraphs;
import com.jingyuyao.tactical.model.state.MapState;
import java.util.Collections;
import java.util.List;

public abstract class Character extends MapObject {

  private final TerrainGraphs terrainGraphs;
  private final Characters characters;
  private final EventBus eventBus;
  private final Stats stats;
  private final List<Item> items;

  Character(
      Coordinate coordinate,
      Multiset<Marker> markers,
      TerrainGraphs terrainGraphs,
      Characters characters,
      EventBus eventBus,
      Stats stats,
      List<Item> items) {
    super(coordinate, markers);
    this.terrainGraphs = terrainGraphs;
    this.characters = characters;
    this.eventBus = eventBus;
    this.stats = stats;
    this.items = items;
  }

  @Override
  public void highlight(MapState mapState) {
    mapState.highlight(this);
  }

  public void registerListener(Object listener) {
    eventBus.register(listener);
  }

  public String getName() {
    return stats.getName();
  }

  public int getHp() {
    return stats.getHp();
  }

  public void damageBy(int delta) {
    stats.damageBy(delta);
    if (stats.isDead()) {
      characters.removeCharacter(this);
      eventBus.post(new RemoveSelf());
    }
  }

  public void healBy(int delta) {
    stats.healBy(delta);
  }

  public void addItem(Item item) {
    items.add(item);
  }

  public void removeItem(Item item) {
    items.remove(item);
  }

  public Iterable<Item> getItems() {
    return items;
  }

  public Iterable<Consumable> getConsumables() {
    return Iterables.filter(items, Consumable.class);
  }

  public Iterable<Weapon> getWeapons() {
    return Iterables.filter(items, Weapon.class);
  }

  public void quickAccess(Item item) {
    int itemIndex = items.indexOf(item);
    Preconditions.checkArgument(itemIndex != -1);
    Collections.swap(items, 0, itemIndex);
  }

  public ListenableFuture<Void> moveAlong(Path path) {
    setCoordinate(path.getDestination());
    SettableFuture<Void> future = SettableFuture.create();
    eventBus.post(new Move(this, future, path));
    return future;
  }

  public void instantMoveTo(Coordinate newCoordinate) {
    setCoordinate(newCoordinate);
    eventBus.post(new InstantMove(this, newCoordinate));
  }

  public ListenableFuture<Void> attacks(Target target) {
    SettableFuture<Void> future = SettableFuture.create();
    eventBus.post(new Attack(target, future));
    return future;
  }

  public Graph<Coordinate> createMoveGraph() {
    return terrainGraphs.distanceFrom(
        getCoordinate(), stats.getMoveDistance(), createMovementPenaltyFunction());
  }

  private Function<Terrain, Integer> createMovementPenaltyFunction() {
    final ImmutableSet<Coordinate> blockedCoordinates = characters.coordinates();
    return new Function<Terrain, Integer>() {
      @Override
      public Integer apply(Terrain input) {
        if (blockedCoordinates.contains(input.getCoordinate())
            || !stats.canPassTerrainType(input.getType())) {
          return TerrainGraphs.BLOCKED;
        }
        return input.getMovementPenalty();
      }
    };
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("name", getName()).toString() + super.toString();
  }
}
