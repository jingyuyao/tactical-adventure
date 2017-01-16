package com.jingyuyao.tactical.model.item;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.battle.PiercingFactory;
import com.jingyuyao.tactical.model.battle.Target;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.common.Algorithms;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.common.Directions;
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
  private final PiercingFactory piercingFactory;

  @Inject
  Weapon(
      EventBus eventBus,
      @Assisted String name,
      @Assisted("usageLeft") int usageLeft,
      @Assisted("attackPower") int attackPower,
      @Assisted Set<Integer> attackDistances,
      Algorithms algorithms,
      Terrains terrains,
      PiercingFactory piercingFactory) {
    super(eventBus, name, usageLeft);
    this.attackPower = attackPower;
    this.attackDistances = attackDistances;
    this.algorithms = algorithms;
    this.terrains = terrains;
    this.piercingFactory = piercingFactory;
  }

  public Set<Integer> getAttackDistances() {
    return attackDistances;
  }

  public int getAttackPower() {
    return attackPower;
  }

  /**
   * Returns whether the {@code owner} holding this weapon can "hit" {@code other} if {@code owner}
   * where to stand on {@code from}.
   * Can "hit" means we can "target" {@code other} and it is within the striking distance.
   */
  public boolean canHitFrom(Character owner, Coordinate from, Character other) {
    return canTarget(owner, other) && targetCoordinatesFor(from).contains(other.getCoordinate());
  }

  /**
   * Return whether the {@code owner} holding this weapon "target" {@code other}.
   */
  public boolean canTarget(Character owner, Character other) {
    return !Objects.equal(owner, other) && !Objects.equal(owner.getClass(), other.getClass());
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

  /**
   * Can shoot in four directions, stops at the first target hit.
   */
  public ImmutableList<Target> getTargets(Coordinate coordinate) {
    ImmutableList.Builder<Target> builder = ImmutableList.builder();
    for (Coordinate direction : Directions.ALL) {
      Optional<Target> targetOptional = piercingFactory.create(this, coordinate, direction);
      if (targetOptional.isPresent()) {
        builder.add(targetOptional.get());
      }
    }
    return builder.build();
  }
}
