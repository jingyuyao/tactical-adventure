package com.jingyuyao.tactical.model.character;

import com.google.common.collect.FluentIterable;
import com.google.common.util.concurrent.ListenableFuture;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.map.Cell;
import com.jingyuyao.tactical.model.map.Path;

public interface Character {

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

  void useItem(Item item);

  ListenableFuture<Void> moveAlong(Path path);

  void instantMoveTo(Cell from, Cell to);
}
