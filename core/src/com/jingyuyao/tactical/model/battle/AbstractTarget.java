package com.jingyuyao.tactical.model.battle;

import com.google.common.collect.ImmutableSet;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.common.Coordinate;

public abstract class AbstractTarget implements Target {

  private final Coordinate select;
  private final ImmutableSet<Coordinate> targetCoordinates;
  private final ImmutableSet<Character> targetCharacters;

  AbstractTarget(
      Coordinate select,
      ImmutableSet<Coordinate> targetCoordinates,
      ImmutableSet<Character> targetCharacters) {
    this.select = select;
    this.targetCoordinates = targetCoordinates;
    this.targetCharacters = targetCharacters;
  }

  @Override
  public Coordinate getSelect() {
    return select;
  }

  @Override
  public ImmutableSet<Coordinate> getTargetCoordinates() {
    return targetCoordinates;
  }

  @Override
  public ImmutableSet<Character> getTargetCharacters() {
    return targetCharacters;
  }
}
