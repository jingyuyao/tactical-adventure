package com.jingyuyao.tactical.model.battle;

import com.google.common.collect.ImmutableSet;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.map.Terrain;

// TODO: need a method that return a "target info" for this target to be displayed
public interface Target {

  Terrain getSelectTerrain();

  ImmutableSet<Terrain> getTargetTerrains();

  ImmutableSet<Character> getTargetCharacters();

  void execute();
}
