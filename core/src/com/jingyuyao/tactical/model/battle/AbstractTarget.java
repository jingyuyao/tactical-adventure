package com.jingyuyao.tactical.model.battle;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.mark.Marking;

public abstract class AbstractTarget implements Target {

  private final Character attacker;
  private final Weapon weapon;
  private final Coordinate selectCoordinate;
  private final ImmutableList<Character> targetCharacters;
  private final Marking marking;

  AbstractTarget(
      Character attacker,
      Weapon weapon,
      Coordinate selectCoordinate,
      ImmutableList<Character> targetCharacters,
      Marking marking) {
    this.attacker = attacker;
    this.weapon = weapon;
    this.selectCoordinate = selectCoordinate;
    this.targetCharacters = targetCharacters;
    this.marking = marking;
  }

  @Override
  public Character getAttacker() {
    return attacker;
  }

  @Override
  public Weapon getWeapon() {
    return weapon;
  }

  @Override
  public Coordinate getSelectCoordinate() {
    return selectCoordinate;
  }

  @Override
  public ImmutableList<Character> getTargetCharacters() {
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
}
