package com.jingyuyao.tactical.model.character;

import com.google.common.collect.FluentIterable;
import com.jingyuyao.tactical.model.item.Item;
import java.util.List;
import java.util.Locale;

abstract class AbstractCharacter implements Character {

  private String name;
  private int maxHp;
  private int hp;
  private int moveDistance;
  private List<Item> items;

  AbstractCharacter() {
  }

  AbstractCharacter(String name, int maxHp, int hp, int moveDistance, List<Item> items) {
    this.name = name;
    this.maxHp = maxHp;
    this.hp = hp;
    this.moveDistance = moveDistance;
    this.items = items;
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
  public void fullHeal() {
    hp = maxHp;
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
  public String toString() {
    return String.format(Locale.US, "%s\nHealth(%d)", name, hp);
  }
}
