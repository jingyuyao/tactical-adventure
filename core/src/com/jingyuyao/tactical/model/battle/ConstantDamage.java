package com.jingyuyao.tactical.model.battle;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.mark.Marking;

// TODO: test me
public class ConstantDamage extends AbstractTarget {

  public ConstantDamage(
      Character attacker,
      Weapon weapon,
      ImmutableSet<Coordinate> selectCoordinates,
      ImmutableList<Character> targetCharacters,
      Marking marking) {
    super(attacker, weapon, selectCoordinates, targetCharacters, marking);
  }

  @Override
  public void execute() {
    for (Character character : getTargetCharacters()) {
      character.damageBy(getWeapon().getAttackPower());
    }
    getWeapon().useOnce();
  }
}
