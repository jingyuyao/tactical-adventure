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

// TODO: test me
@Singleton
public class PiercingTargetFactory extends AbstractTargetFactory {

  @Inject
  PiercingTargetFactory(Characters characters, Terrains terrains) {
    super(characters, terrains);
  }

  @Override
  Optional<Target> create(final Character attacker, Weapon weapon, Coordinate direction) {
    ImmutableSet.Builder<Coordinate> targetBuilder = ImmutableSet.builder();
    Coordinate current = attacker.getCoordinate().offsetBy(direction).offsetBy(direction);

    while (getTerrains().contains(current)) {
      targetBuilder.add(current);
      current = current.offsetBy(direction);
    }

    final ImmutableSet<Coordinate> targetCoordinates = targetBuilder.build();
    if (targetCoordinates.isEmpty()) {
      return Optional.absent();
    }

    ImmutableList<Character> targetCharacters = ImmutableList.copyOf(
        Iterables.filter(getCharacters(), new Predicate<Character>() {
          @Override
          public boolean apply(Character input) {
            return targetCoordinates.contains(input.getCoordinate());
          }
        })
    );

    Marking marking = createMarking(targetCoordinates, targetCharacters);

    return Optional.<Target>of(
        new ConstantDamage(attacker, weapon, targetCoordinates, targetCharacters, marking));
  }
}
