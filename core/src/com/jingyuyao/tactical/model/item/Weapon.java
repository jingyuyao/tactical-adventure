package com.jingyuyao.tactical.model.item;

import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.Character;
import java.util.Set;
import javax.inject.Inject;

/**
 * An {@link Item} that can affect a {@link Character}'s HP and status. Not a {@link Consumable}
 * since the effect of a {@link Weapon} depends on its user.
 */
public class Weapon extends Usable {

  private final int attackPower;
  // TODO: at some point we need to change this to some like a function to provides coordinates
  private final Set<Integer> attackDistances;

  @Inject
  Weapon(
      EventBus eventBus,
      @Assisted String name,
      @Assisted("usageLeft") int usageLeft,
      @Assisted("attackPower") int attackPower,
      @Assisted Set<Integer> attackDistances) {
    super(eventBus, name, usageLeft);
    this.attackPower = attackPower;
    this.attackDistances = attackDistances;
  }

  // TODO: this should be a function of a coordinate and terrains
  public Set<Integer> getAttackDistances() {
    return attackDistances;
  }

  public int getAttackPower() {
    return attackPower;
  }
}
