package com.jingyuyao.tactical.model.character;

import com.google.common.base.MoreObjects;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.jingyuyao.tactical.model.character.event.InstantMove;
import com.jingyuyao.tactical.model.character.event.Move;
import com.jingyuyao.tactical.model.character.event.RemoveCharacter;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.common.Disposable;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.map.Path;
import com.jingyuyao.tactical.model.map.Terrain;
import com.jingyuyao.tactical.model.state.MapState;

public abstract class Character extends MapObject implements Disposable {

  private final Stats stats;
  private final Items items;

  Character(EventBus eventBus, Coordinate coordinate, Stats stats, Items items) {
    super(eventBus, coordinate);
    this.stats = stats;
    this.items = items;
  }

  @Override
  public void dispose() {
    items.dispose();
  }

  @Override
  public void highlight(MapState mapState) {
    mapState.highlight(this);
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
    boolean dead = stats.damageBy(delta);
    if (dead) {
      post(new RemoveCharacter(this));
      dispose();
    }
  }

  public void healBy(int delta) {
    stats.healBy(delta);
  }

  public Iterable<Item> getItems() {
    return items.getItems();
  }

  public Iterable<Consumable> getConsumables() {
    return items.getConsumables();
  }

  public Iterable<Weapon> getWeapons() {
    return items.getWeapons();
  }

  public void quickAccess(Item item) {
    items.quickAccess(item);
  }

  public ListenableFuture<Void> move(Path path) {
    setCoordinate(path.getDestination());
    SettableFuture<Void> future = SettableFuture.create();
    post(new Move(this, path, future));
    return future;
  }

  public void instantMoveTo(Coordinate newCoordinate) {
    setCoordinate(newCoordinate);
    post(new InstantMove(this, newCoordinate));
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("name", getName()).toString() + super.toString();
  }
}
