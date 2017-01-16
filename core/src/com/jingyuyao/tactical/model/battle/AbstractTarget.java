package com.jingyuyao.tactical.model.battle;

import com.google.common.collect.ImmutableSet;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.map.Terrain;

public abstract class AbstractTarget implements Target {

  private final Terrain select;
  private final ImmutableSet<Terrain> targetTerrains;
  private final ImmutableSet<Character> targetCharacters;

  AbstractTarget(
      Terrain select,
      ImmutableSet<Terrain> targetTerrains,
      ImmutableSet<Character> targetCharacters) {
    this.select = select;
    this.targetTerrains = targetTerrains;
    this.targetCharacters = targetCharacters;
  }

  @Override
  public Terrain getSelectTerrain() {
    return select;
  }

  @Override
  public ImmutableSet<Terrain> getTargetTerrains() {
    return targetTerrains;
  }

  @Override
  public ImmutableSet<Character> getTargetCharacters() {
    return targetCharacters;
  }
}
