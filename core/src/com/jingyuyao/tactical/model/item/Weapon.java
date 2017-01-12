package com.jingyuyao.tactical.model.item;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.common.Algorithms;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.map.Terrains;
import java.util.Set;
import javax.inject.Inject;

/**
 * An {@link Item} that can affect a {@link Character}'s HP and status. Not a {@link Consumable}
 * since the effect of a {@link Weapon} depends on its user.
 */
public class Weapon extends Usable {

  private final int attackPower;
  private final Set<Integer> attackDistances;
  private final Algorithms algorithms;
  private final Terrains terrains;

  @Inject
  Weapon(
      EventBus eventBus,
      @Assisted String name,
      @Assisted("usageLeft") int usageLeft,
      @Assisted("attackPower") int attackPower,
      @Assisted Set<Integer> attackDistances,
      Algorithms algorithms,
      Terrains terrains) {
    super(eventBus, name, usageLeft);
    this.attackPower = attackPower;
    this.attackDistances = attackDistances;
    this.algorithms = algorithms;
    this.terrains = terrains;
  }

  public Set<Integer> getAttackDistances() {
    return attackDistances;
  }

  public int getAttackPower() {
    return attackPower;
  }

  /**
   * Return whether the {@code owner} holding this weapon "target" {@code other}.
   */
  public boolean canTarget(Character owner, Character other) {
    return !Objects.equal(owner, other)
        && !Objects.equal(owner.getClass(), other.getClass())
        && targetCoordinatesFor(owner.getCoordinate()).contains(other.getCoordinate());
  }

  public void hit(Character character) {
    // Actual "hitting" is done by weapon so different weapon can impose different effects
    // on the character. Default implementation just damages the character.
    character.damageBy(attackPower);
    useOnce();
  }

  /**
   * Return a set of {@link Coordinate}s this weapon can target if the attack originates from
   * {@code coordinate}.
   */
  // TODO: this should be more flexible to allow for different "shapes" of target areas
  public ImmutableSet<Coordinate> targetCoordinatesFor(Coordinate coordinate) {
    ImmutableSet.Builder<Coordinate> builder = new Builder<Coordinate>();
    for (Integer distance : attackDistances) {
      builder.addAll(
          algorithms
              .getNDistanceAway(terrains.getWidth(), terrains.getHeight(), coordinate, distance)
      );
    }
    return builder.build();
  }
}
