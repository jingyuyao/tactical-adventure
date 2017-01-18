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

public class Melee extends DirectionalWeapon {

  private final Characters characters;
  private final Terrains terrains;

  @Inject
  Melee(
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
    final Coordinate targetCoordinate = attacker.getCoordinate().offsetBy(direction);
    if (!terrains.contains(targetCoordinate)) {
      return Optional.absent();
    }
    ImmutableList<Character> targetCharacters = ImmutableList.copyOf(Iterables.filter(
        characters, new Predicate<Character>() {
          @Override
          public boolean apply(Character input) {
            return input.getCoordinate().equals(targetCoordinate);
          }
        }));
    ImmutableSet<Coordinate> targetCoordinates = ImmutableSet.of(targetCoordinate);
    Marking marking = createMarking(terrains.getAll(targetCoordinates), targetCharacters);

    return Optional.<Target>of(
        new ConstantDamage(attacker, this, targetCoordinates, targetCharacters, marking));
  }
}
