package com.jingyuyao.tactical.model.character;

import com.google.common.collect.FluentIterable;
import com.jingyuyao.tactical.model.item.Item;
import java.util.Locale;

class BaseCharacter implements Character {

  private String name;
  private int maxHp;
  private int hp;
  private int moveDistance;
  private Items items;

  BaseCharacter() {
  }

  BaseCharacter(String name, int maxHp, int hp, int moveDistance, Items items) {
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
  public FluentIterable<Item> fluentItems() {
    return FluentIterable.from(items.getUnequippedItems());
  }

  @Override
  public void useItem(Item item) {
    items.useUnequippedItem(item);
  }

  @Override
  public String toString() {
    return String.format(Locale.US, "%s\nHealth(%d)", name, hp);
  }
}
