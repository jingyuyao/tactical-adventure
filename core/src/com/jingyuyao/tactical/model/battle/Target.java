package com.jingyuyao.tactical.model.battle;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.common.Coordinate;

// TODO: need a method that return a "target info" for this target to be displayed
public interface Target {

  ImmutableSet<Coordinate> getSelectCoordinates();

  ImmutableList<Character> getTargetCharacters();

  void showMarking();

  void hideMarking();

  void execute();
}
