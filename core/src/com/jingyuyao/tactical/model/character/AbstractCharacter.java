package com.jingyuyao.tactical.model.character;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multiset;
import com.google.common.eventbus.EventBus;
import com.google.common.graph.Graph;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
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
import com.jingyuyao.tactical.model.map.AbstractMapObject;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.map.Marker;
import com.jingyuyao.tactical.model.map.Path;
import com.jingyuyao.tactical.model.map.TerrainGraphs;
import com.jingyuyao.tactical.model.state.MapState;
import com.jingyuyao.tactical.model.terrain.Terrain;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

abstract class AbstractCharacter<T extends CharacterData>
    extends AbstractMapObject<T> implements Character {

  private final List<Item> items;
  private final TerrainGraphs terrainGraphs;
  private final Characters characters;
  private final EventBus eventBus;

  AbstractCharacter(
      T data,
      Multiset<Marker> markers,
      List<Item> items,
      EventBus eventBus,
      TerrainGraphs terrainGraphs,
      Characters characters) {
    super(data, markers);
    this.terrainGraphs = terrainGraphs;
    this.characters = characters;
    this.eventBus = eventBus;
    this.items = items;
  }

  @Override
  public void highlight(MapState mapState) {
    mapState.highlight(this);
  }

  @Override
  public void registerListener(Object listener) {
    eventBus.register(listener);
  }

  @Override
  public String getName() {
    return getData().getName();
  }

  @Override
  public void damageBy(int delta) {
    getData().damageBy(delta);
    if (getData().isDead()) {
      characters.remove(this);
      eventBus.post(new RemoveSelf());
    }
  }

  @Override
  public void healBy(int delta) {
    getData().healBy(delta);
  }

  @Override
  public void addItem(Item item) {
    items.add(item);
  }

  @Override
  public void removeItem(Item item) {
    items.remove(item);
  }

  @Override
  public void quickAccess(Item item) {
    int itemIndex = items.indexOf(item);
    Preconditions.checkArgument(itemIndex != -1);
    Collections.swap(items, 0, itemIndex);
  }

  @Override
  public Iterable<Item> getItems() {
    return items;
  }

  @Override
  public Iterable<Weapon> getWeapons() {
    return Iterables.filter(items, Weapon.class);
  }

  @Override
  public Iterable<Consumable> getConsumables() {
    return Iterables.filter(items, Consumable.class);
  }

  @Override
  public ListenableFuture<Void> attacks(final Weapon weapon, final Target target) {
    SettableFuture<Void> future = SettableFuture.create();
    eventBus.post(new Attack(target, future));
    Futures.addCallback(future, new FutureCallback<Void>() {
      @Override
      public void onSuccess(Void result) {
        weapon.damages(target);
        useThenRemoveIfBroken(weapon);
      }

      @Override
      public void onFailure(Throwable t) {

      }
    });
    return future;
  }

  @Override
  public void consumes(Consumable consumable) {
    // TODO: we'll probably need to return a future here when we have animation for consumables
    consumable.apply(this);
    useThenRemoveIfBroken(consumable);
  }

  @Override
  public ListenableFuture<Void> moveAlong(Path path) {
    setCoordinate(path.getDestination());
    SettableFuture<Void> future = SettableFuture.create();
    eventBus.post(new Move(this, future, path));
    return future;
  }

  @Override
  public void instantMoveTo(Coordinate newCoordinate) {
    setCoordinate(newCoordinate);
    eventBus.post(new InstantMove(this, newCoordinate));
  }

  @Override
  public Graph<Coordinate> createMoveGraph() {
    return terrainGraphs.distanceFrom(
        getCoordinate(), getData().getMoveDistance(), createMovementPenaltyFunction());
  }

  private Function<Terrain, Integer> createMovementPenaltyFunction() {
    final ImmutableSet<Coordinate> blocked = ImmutableSet.copyOf(characters.coordinates());
    return new Function<Terrain, Integer>() {
      @Override
      public Integer apply(Terrain input) {
        if (blocked.contains(input.getCoordinate())
            || !input.canHold(AbstractCharacter.this)) {
          return TerrainGraphs.BLOCKED;
        }
        return input.getMovementPenalty();
      }
    };
  }

  private void useThenRemoveIfBroken(Item item) {
    item.useOnce();
    if (item.getUsageLeft() == 0) {
      items.remove(item);
    }
  }

  @Override
  public String toString() {
    return String.format(Locale.US, "%s\nHealth(%d)", getName(), getData().getHp());
  }
}
