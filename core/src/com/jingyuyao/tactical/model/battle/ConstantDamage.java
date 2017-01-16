package com.jingyuyao.tactical.model.battle;

import com.google.common.collect.ImmutableSet;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.item.Weapon;

public class ConstantDamage extends AbstractTarget {

  ConstantDamage(
      Coordinate select,
      ImmutableSet<Coordinate> targets,
      ImmutableSet<Character> characters) {
    super(select, targets, characters);
  }

  @Override
  public void execute(Weapon weapon) {
    for (Character character : getTargetCharacters()) {
      weapon.hit(character);
    }
  }
}
