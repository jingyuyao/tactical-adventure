package com.jingyuyao.tactical.model.item;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.battle.ConstantDamage;
import com.jingyuyao.tactical.model.battle.Target;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.map.Terrains;
import com.jingyuyao.tactical.model.mark.Marking;
import javax.inject.Inject;

public class PiercingLaser extends DirectionalWeapon {

  private final Characters characters;
  private final Terrains terrains;

  @Inject
  PiercingLaser(
      EventBus eventBus,
      @Assisted String name,
      @Assisted("usageLeft") int usageLeft,
      @Assisted("attackPower") int attackPower,
      Characters characters,
      Terrains terrains) {
    super(eventBus, name, usageLeft, attackPower);
    this.characters = characters;
    this.terrains = terrains;
  }

  @Override
  Optional<Target> createTarget(Character attacker, Coordinate direction) {
    ImmutableSet.Builder<Coordinate> targetBuilder = ImmutableSet.builder();
    Coordinate current = attacker.getCoordinate().offsetBy(direction).offsetBy(direction);

    while (terrains.contains(current)) {
      targetBuilder.add(current);
      current = current.offsetBy(direction);
    }

    final ImmutableSet<Coordinate> targetCoordinates = targetBuilder.build();
    if (targetCoordinates.isEmpty()) {
      return Optional.absent();
    }

    ImmutableList<Character> targetCharacters = ImmutableList.copyOf(
        Iterables.filter(characters, new Predicate<Character>() {
          @Override
          public boolean apply(Character input) {
            return targetCoordinates.contains(input.getCoordinate());
          }
        })
    );

    Marking marking = createMarking(terrains.getAll(targetCoordinates), targetCharacters);

    return Optional.<Target>of(
        new ConstantDamage(attacker, this, targetCoordinates, targetCharacters, marking));
  }
}
