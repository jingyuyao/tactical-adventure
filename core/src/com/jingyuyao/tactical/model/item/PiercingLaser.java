package com.jingyuyao.tactical.model.item;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.map.Terrains;
import com.jingyuyao.tactical.model.target.Target;
import com.jingyuyao.tactical.model.target.TargetFactory;
import javax.inject.Inject;

class PiercingLaser extends DirectionalWeapon {

  private final Terrains terrains;
  private final TargetFactory targetFactory;

  @Inject
  PiercingLaser(
      EventBus eventBus,
      @Assisted String name,
      @Assisted("usageLeft") int usageLeft,
      @Assisted("attackPower") int attackPower,
      Terrains terrains,
      TargetFactory targetFactory) {
    super(eventBus, name, usageLeft, attackPower);
    this.targetFactory = targetFactory;
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

    return Optional.of(
        targetFactory.create(targetCoordinates, targetCoordinates));
  }
}
