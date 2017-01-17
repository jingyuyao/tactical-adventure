package com.jingyuyao.tactical.model.battle;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.map.Terrains;
import com.jingyuyao.tactical.model.mark.Marking;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class ImmediateTargetFactory extends AbstractTargetFactory {

  @Inject
  ImmediateTargetFactory(Characters characters, Terrains terrains) {
    super(characters, terrains);
  }

  @Override
  Optional<Target> create(Character attacker, Weapon weapon, Coordinate direction) {
    final Coordinate targetCoordinate = attacker.getCoordinate().offsetBy(direction);
    if (!getTerrains().contains(targetCoordinate)) {
      return Optional.absent();
    }
    ImmutableList<Character> targetCharacters = ImmutableList.copyOf(Iterables.filter(
        getCharacters(), new Predicate<Character>() {
          @Override
          public boolean apply(Character input) {
            return input.getCoordinate().equals(targetCoordinate);
          }
        }));
    ImmutableSet<Coordinate> targetCoordinates = ImmutableSet.of(targetCoordinate);
    Marking marking = createMarking(targetCoordinates, targetCharacters);

    return Optional.<Target>of(
        new ConstantDamage(attacker, weapon, targetCoordinates, targetCharacters, marking));
  }
}
