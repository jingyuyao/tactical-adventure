package com.jingyuyao.tactical.model.battle;

import com.google.common.collect.ImmutableSet;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.Terrain;

public interface Target {

  Terrain getSelectTerrain();

  ImmutableSet<Terrain> getTargetTerrains();

  ImmutableSet<Character> getTargetCharacters();

  void execute(Weapon weapon);
}
