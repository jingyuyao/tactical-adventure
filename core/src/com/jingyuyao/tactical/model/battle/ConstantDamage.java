package com.jingyuyao.tactical.model.battle;

import com.google.common.collect.ImmutableSet;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.mark.Marking;

// TODO: test me
public class ConstantDamage extends AbstractTarget {

  ConstantDamage(Weapon weapon, Coordinate selectCoordinate,
      ImmutableSet<Character> targetCharacters, Marking marking) {
    super(weapon, selectCoordinate, targetCharacters, marking);
  }

  @Override
  public void execute() {
    for (Character character : getTargetCharacters()) {
      getWeapon().hit(character);
    }
  }
}
