package com.jingyuyao.tactical.model.battle;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.item.Weapon;

// TODO: need a method that return a "target info" for this target to be displayed
public interface Target {

  Character getAttacker();

  Weapon getWeapon();

  Coordinate getSelectCoordinate();

  ImmutableList<Character> getTargetCharacters();

  void showMarking();

  void hideMarking();

  void execute();
}
