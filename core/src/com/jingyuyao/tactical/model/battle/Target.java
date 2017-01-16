package com.jingyuyao.tactical.model.battle;

import com.google.common.collect.ImmutableSet;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.item.Weapon;

public interface Target {

  Coordinate getSelect();

  ImmutableSet<Coordinate> getTargetCoordinates();

  ImmutableSet<Character> getTargetCharacters();

  void execute(Weapon weapon);
}
