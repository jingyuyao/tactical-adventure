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
    Coordinate current = select;
    while (terrains.contains(current)) {
      targetBuilder.add(current);
      current = current.offsetBy(direction);
    }

    final ImmutableSet<Coordinate> targetCoordiantes = targetBuilder.build();
    if (targetCoordiantes.isEmpty()) {
      return Optional.absent();
    }

    ImmutableSet<Character> targetCharacters = ImmutableSet.copyOf(
        Iterables.filter(characters, new Predicate<Character>() {
          @Override
          public boolean apply(Character input) {
            return targetCoordiantes.contains(input.getCoordinate());
          }
        })
    );

    return Optional.<Target>of(new ConstantDamage(select, targetCoordiantes, targetCharacters));
  }
}
