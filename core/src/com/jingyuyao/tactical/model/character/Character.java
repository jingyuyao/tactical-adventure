package com.jingyuyao.tactical.model.character;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.jingyuyao.tactical.model.character.event.InstantMove;
import com.jingyuyao.tactical.model.character.event.Move;
import com.jingyuyao.tactical.model.character.event.RemoveCharacter;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.common.Disposable;
import com.jingyuyao.tactical.model.event.AbstractEvent;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.item.event.RemoveItem;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.map.Path;
import com.jingyuyao.tactical.model.map.Terrain;
import com.jingyuyao.tactical.model.state.MapState;
import java.util.Collections;
import java.util.List;

public abstract class Character extends MapObject implements Disposable {

  private final EventBus eventBus;
  private final Stats stats;
  private final List<Item> items;

  Character(EventBus eventBus, Coordinate coordinate, Stats stats, List<Item> items) {
    super(coordinate);
    this.eventBus = eventBus;
    this.stats = stats;
    this.items = items;
    eventBus.register(this);
  }

  @Override
  public void dispose() {
    eventBus.unregister(this);
  }

  @Override
  public void highlight(MapState mapState) {
    mapState.highlight(this);
  }

  @Subscribe
  public void removeItem(RemoveItem removeItem) {
    // TODO: how to make this constant time?
    items.remove(removeItem.getObject());
  }

  public void post(AbstractEvent<?> event) {
    eventBus.post(event);
  }

  public String getName() {
    return stats.getName();
  }

  public int getHp() {
    return stats.getHp();
  }

  public int getMoveDistance() {
    return stats.getMoveDistance();
  }

  public boolean canPassTerrainType(Terrain.Type terrainType) {
    return stats.canPassTerrainType(terrainType);
  }

  public void damageBy(int delta) {
    stats.damageBy(delta);
    if (stats.isDead()) {
      eventBus.post(new RemoveCharacter(this));
      dispose();
    }
  }

  public void healBy(int delta) {
    stats.healBy(delta);
  }

  public boolean isDead() {
    return stats.isDead();
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
    Preconditions.checkArgument(items.contains(item));
    Collections.swap(items, 0, items.indexOf(item));
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

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("name", getName()).toString() + super.toString();
  }
}
