package com.jingyuyao.tactical.model.character;

import com.google.common.collect.FluentIterable;
import com.google.common.util.concurrent.ListenableFuture;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.map.Path;

public interface Character extends MapObject {

  void registerListener(Object listener);

  String getName();

  int getHp();

  int getMoveDistance();

  void damageBy(int delta);

  void healBy(int delta);

  void addItem(Item item);

  void removeItem(Item item);

  void quickAccess(Item item);

  FluentIterable<Item> fluentItems();

  ListenableFuture<Void> attacks(Weapon weapon, Target target);

  void consumes(Consumable consumable);

  ListenableFuture<Void> moveAlong(Path path);

  void instantMoveTo(Coordinate coordinate);
}
