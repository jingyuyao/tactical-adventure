package com.jingyuyao.tactical.model.item;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.common.Directions;
import com.jingyuyao.tactical.model.target.Target;

/**
 * A weapon that can be targeted in all directions in {@link Directions#ALL}.
 */
public abstract class DirectionalWeapon extends Weapon {

  DirectionalWeapon(
      EventBus eventBus,
      @Assisted String name,
      @Assisted("usageLeft") int usageLeft,
      @Assisted("attackPower") int attackPower) {
    super(eventBus, name, usageLeft, attackPower);
  }

  @Override
  public ImmutableList<Target> createTargets(Character attacker) {
    ImmutableList.Builder<Target> builder = ImmutableList.builder();
    for (Coordinate direction : Directions.ALL) {
      Optional<Target> targetOptional = createTarget(attacker, direction);
      if (targetOptional.isPresent()) {
        builder.add(targetOptional.get());
      }
    }
    return builder.build();
  }

  abstract Optional<Target> createTarget(Character attacker, Coordinate direction);
}
