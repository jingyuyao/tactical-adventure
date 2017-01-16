package com.jingyuyao.tactical.model.battle;

import com.google.common.collect.ImmutableSet;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.mark.Marking;

public abstract class AbstractTarget implements Target {

  private final Weapon weapon;
  private final Coordinate selectCoordinate;
  private final ImmutableSet<Character> targetCharacters;
  private final Marking marking;

  AbstractTarget(
      Weapon weapon,
      Coordinate selectCoordinate,
      ImmutableSet<Character> targetCharacters, Marking marking) {
    this.weapon = weapon;
    this.selectCoordinate = selectCoordinate;
    this.targetCharacters = targetCharacters;
    this.marking = marking;
  }

  @Override
  public Coordinate getSelectCoordinate() {
    return selectCoordinate;
  }

  @Override
  public ImmutableSet<Character> getTargetCharacters() {
    return targetCharacters;
  }

  @Override
  public void showMarking() {
    marking.apply();
  }

  @Override
  public void hideMarking() {
    marking.clear();
  }

  Weapon getWeapon() {
    return weapon;
  }
}
