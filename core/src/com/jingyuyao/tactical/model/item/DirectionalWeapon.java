package com.jingyuyao.tactical.model.item;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.common.Directions;

/**
 * A weapon that can be targeted in all directions in {@link Directions#ALL}.
 */
abstract class DirectionalWeapon extends AbstractWeapon {

  DirectionalWeapon(EventBus eventBus, @Assisted WeaponStats weaponInfo) {
    super(eventBus, weaponInfo);
  }

  @Override
  public ImmutableList<Target> createTargets(Coordinate from) {
    ImmutableList.Builder<Target> builder = ImmutableList.builder();
    for (Coordinate direction : Directions.ALL) {
      Optional<Target> targetOptional = createTarget(from, direction);
      if (targetOptional.isPresent()) {
        builder.add(targetOptional.get());
      }
    }
    return builder.build();
  }

  abstract Optional<Target> createTarget(Coordinate from, Coordinate direction);
}
