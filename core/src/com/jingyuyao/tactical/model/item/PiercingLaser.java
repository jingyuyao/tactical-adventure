package com.jingyuyao.tactical.model.item;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.map.Terrains;
import javax.inject.Inject;

class PiercingLaser extends DirectionalWeapon {

  private final Terrains terrains;
  private final TargetFactory targetFactory;

  @Inject
  PiercingLaser(
      EventBus eventBus,
      @Assisted WeaponStats weaponInfo,
      Terrains terrains,
      TargetFactory targetFactory) {
    super(eventBus, weaponInfo);
    this.targetFactory = targetFactory;
    this.terrains = terrains;
  }

  @Override
  Optional<Target> createTarget(Coordinate from, Coordinate direction) {
    ImmutableSet.Builder<Coordinate> targetBuilder = ImmutableSet.builder();
    Coordinate current = from.offsetBy(direction).offsetBy(direction);

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
