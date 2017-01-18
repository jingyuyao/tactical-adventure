package com.jingyuyao.tactical.model.target;

import com.google.common.collect.ImmutableSet;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.item.Weapon;

public interface TargetFactory {

  @Named("ConstantDamage")
  Target createConstantDamage(
      Character attacker,
      Weapon weapon,
      @Assisted("select") ImmutableSet<Coordinate> selectCoordinates,
      @Assisted("target") ImmutableSet<Coordinate> targetCoordinates);
}
