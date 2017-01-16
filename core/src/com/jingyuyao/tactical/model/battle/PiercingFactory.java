package com.jingyuyao.tactical.model.battle;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.map.Terrains;
import javax.inject.Inject;
import javax.inject.Singleton;

// TODO: test me
@Singleton
public class PiercingFactory {

  private final Characters characters;
  private final Terrains terrains;

  @Inject
  PiercingFactory(Characters characters, Terrains terrains) {
    this.characters = characters;
    this.terrains = terrains;
  }

  public Optional<Target> create(Coordinate origin, Coordinate direction) {
    ImmutableSet.Builder<Coordinate> targetBuilder = ImmutableSet.builder();
    Coordinate select = origin.offsetBy(direction);
    Coordinate current = select.offsetBy(direction);
    while (terrains.contains(current)) {
      targetBuilder.add(current);
      current = current.offsetBy(direction);
    }

    final ImmutableSet<Coordinate> targetCoordinates = targetBuilder.build();
    if (targetCoordinates.isEmpty()) {
      return Optional.absent();
    }

    ImmutableSet<Character> targetCharacters = ImmutableSet.copyOf(
        Iterables.filter(characters, new Predicate<Character>() {
          @Override
          public boolean apply(Character input) {
            return targetCoordinates.contains(input.getCoordinate());
          }
        })
    );

    return Optional.<Target>of(
        new ConstantDamage(
            terrains.get(select),
            ImmutableSet.copyOf(terrains.getAll(targetCoordinates)),
            targetCharacters)
    );
  }
}
