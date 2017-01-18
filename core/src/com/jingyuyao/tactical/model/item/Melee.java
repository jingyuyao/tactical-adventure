package com.jingyuyao.tactical.model.item;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.map.Terrains;
import com.jingyuyao.tactical.model.target.Target;
import com.jingyuyao.tactical.model.target.TargetFactory;
import javax.inject.Inject;

class Melee extends DirectionalWeapon {

  private final Terrains terrains;
  private final TargetFactory targetFactory;

  @Inject
  Melee(
      EventBus eventBus,
      @Assisted String name,
      @Assisted("usageLeft") int usageLeft,
      @Assisted("attackPower") int attackPower,
      Terrains terrains,
      TargetFactory targetFactory) {
    super(eventBus, name, usageLeft, attackPower);
    this.terrains = terrains;
    this.targetFactory = targetFactory;
  }

  @Override
  Optional<Target> createTarget(Coordinate from, Coordinate direction) {
    final Coordinate targetCoordinate = from.offsetBy(direction);
    if (!terrains.contains(targetCoordinate)) {
      return Optional.absent();
    }
    ImmutableSet<Coordinate> targetCoordinates = ImmutableSet.of(targetCoordinate);

    return Optional.of(
        targetFactory.create(targetCoordinates, targetCoordinates));
  }
}
