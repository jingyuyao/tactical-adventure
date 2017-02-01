package com.jingyuyao.tactical.model.character;

import com.google.common.base.Preconditions;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Multiset;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.jingyuyao.tactical.model.character.event.Attack;
import com.jingyuyao.tactical.model.character.event.InstantMove;
import com.jingyuyao.tactical.model.character.event.Move;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.AbstractMapObject;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.map.Marker;
import com.jingyuyao.tactical.model.map.Path;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

abstract class AbstractCharacter extends AbstractMapObject implements Character {

  private transient final EventBus eventBus;
  private String name;
  private int maxHp;
  private int hp;
  private int moveDistance;
  private List<Item> items;

  AbstractCharacter(Multiset<Marker> markers, EventBus eventBus) {
    super(markers);
    this.eventBus = eventBus;
  }

  AbstractCharacter(
      Coordinate coordinate, Multiset<Marker> markers, EventBus eventBus,
      String name, int maxHp, int hp, int moveDistance, List<Item> items) {
    super(coordinate, markers);
    this.eventBus = eventBus;
    this.name = name;
    this.maxHp = maxHp;
    this.hp = hp;
    this.moveDistance = moveDistance;
    this.items = items;
  }

  @Override
  public void registerListener(Object listener) {
    eventBus.register(listener);
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public int getHp() {
    return hp;
  }

  @Override
  public int getMoveDistance() {
    return moveDistance;
  }

  @Override
  public void damageBy(int delta) {
    hp = Math.max(hp - delta, 0);
  }

  @Override
  public void healBy(int delta) {
    hp = Math.min(hp + delta, maxHp);
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
  public FluentIterable<Item> fluentItems() {
    return FluentIterable.from(items);
  }

  @Override
  public void useItem(Item item) {
    item.useOnce();
    if (item.getUsageLeft() == 0) {
      items.remove(item);
    }
  }

  @Override
  public ListenableFuture<Void> attacks(final Weapon weapon, final Target target) {
    SettableFuture<Void> future = SettableFuture.create();
    eventBus.post(new Attack(target, future));
    Futures.addCallback(future, new FutureCallback<Void>() {
      @Override
      public void onSuccess(Void result) {
        weapon.damages(target);
        useItem(weapon);
      }

      @Override
      public void onFailure(Throwable t) {

      }
    });
    return future;
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
  public String toString() {
    return String.format(Locale.US, "%s\nHealth(%d)", name, hp);
  }
}
