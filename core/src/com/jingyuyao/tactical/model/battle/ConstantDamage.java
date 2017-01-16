package com.jingyuyao.tactical.model.battle;

import com.google.common.collect.ImmutableSet;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.Terrain;

// TODO: test me
public class ConstantDamage extends AbstractTarget {

  ConstantDamage(
      Weapon weapon,
      Terrain select,
      ImmutableSet<Terrain> targetTerrains,
      ImmutableSet<Character> targetCharacters) {
    super(weapon, select, targetTerrains, targetCharacters);
  }

  @Override
  public void execute() {
    for (Character character : getTargetCharacters()) {
      getWeapon().hit(character);
    }
  }
}
