package com.jingyuyao.tactical.model.item;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.battle.PiercingFactory;
import com.jingyuyao.tactical.model.battle.Target;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.common.Directions;
import javax.inject.Inject;

/**
 * An {@link Item} that can affect a {@link Character}'s HP and status. Not a {@link Consumable}
 * since the effect of a {@link Weapon} depends on its user.
 */
public class Weapon extends Usable {

  private final int attackPower;
  private final PiercingFactory piercingFactory;

  @Inject
  Weapon(
      EventBus eventBus,
      @Assisted String name,
      @Assisted("usageLeft") int usageLeft,
      @Assisted("attackPower") int attackPower,
      PiercingFactory piercingFactory) {
    super(eventBus, name, usageLeft);
    this.attackPower = attackPower;
    this.piercingFactory = piercingFactory;
  }

  public int getAttackPower() {
    return attackPower;
  }

  public ImmutableList<Target> createTargets(Character attacker) {
    ImmutableList.Builder<Target> builder = ImmutableList.builder();
    for (Coordinate direction : Directions.ALL) {
      Optional<Target> targetOptional =
          piercingFactory.create(attacker, this, direction);
      if (targetOptional.isPresent()) {
        builder.add(targetOptional.get());
      }
    }
    return builder.build();
  }
}
