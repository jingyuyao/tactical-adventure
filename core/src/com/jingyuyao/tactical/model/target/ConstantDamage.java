package com.jingyuyao.tactical.model.target;

import com.google.common.collect.ImmutableSet;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.map.Terrains;
import javax.inject.Inject;

// TODO: test me
class ConstantDamage extends AbstractTarget {

  @Inject
  ConstantDamage(
      @Assisted Character attacker,
      @Assisted Weapon weapon,
      @Assisted("select") ImmutableSet<Coordinate> selectCoordinates,
      @Assisted("target") ImmutableSet<Coordinate> targetCoordinates,
      Characters characters,
      Terrains terrains) {
    super(attacker, weapon, selectCoordinates, targetCoordinates, characters, terrains);
  }

  @Override
  public void execute() {
    for (Character character : getTargetCharacters()) {
      character.damageBy(getWeapon().getAttackPower());
    }
    getWeapon().useOnce();
  }
}
