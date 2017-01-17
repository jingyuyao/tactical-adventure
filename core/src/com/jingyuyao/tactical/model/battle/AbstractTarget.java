package com.jingyuyao.tactical.model.battle;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.mark.Marking;

public abstract class AbstractTarget implements Target {

  private final Character attacker;
  private final Weapon weapon;
  private final ImmutableSet<Coordinate> selectCoordinates;
  private final ImmutableList<Character> targetCharacters;
  private final Marking marking;

  AbstractTarget(
      Character attacker,
      Weapon weapon,
      ImmutableSet<Coordinate> selectCoordinates,
      ImmutableList<Character> targetCharacters,
      Marking marking) {
    this.attacker = attacker;
    this.weapon = weapon;
    this.selectCoordinates = selectCoordinates;
    this.targetCharacters = targetCharacters;
    this.marking = marking;
  }

  Character getAttacker() {
    return attacker;
  }

  Weapon getWeapon() {
    return weapon;
  }

  @Override
  public ImmutableSet<Coordinate> getSelectCoordinates() {
    return selectCoordinates;
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
