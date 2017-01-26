package com.jingyuyao.tactical.model.item;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.map.Directions;
import com.jingyuyao.tactical.model.map.Terrains;
import javax.inject.Inject;

/**
 * A weapon that can be targeted in all directions in {@link Directions#ALL}.
 */
// TODO: test me
public class DirectionalWeapon extends AbstractWeapon<DirectionalWeaponStats> {

  private final Terrains terrains;
  private final TargetFactory targetFactory;

  @Inject
  DirectionalWeapon(
      @Assisted Character owner,
      @Assisted DirectionalWeaponStats weaponStats,
      Terrains terrains,
      TargetFactory targetFactory) {
    super(owner, weaponStats);
    this.terrains = terrains;
    this.targetFactory = targetFactory;
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

  private Optional<Target> createTarget(Coordinate from, Coordinate direction) {
    Coordinate current = from.offsetBy(direction);
    ImmutableSet<Coordinate> selectCoordinates = ImmutableSet.of(current);
    int leftOverDistance = getItemStats().getDistance();

    ImmutableSet.Builder<Coordinate> targetBuilder = ImmutableSet.builder();
    while (leftOverDistance > 0 && terrains.contains(current)) {
      targetBuilder.add(current);
      current = current.offsetBy(direction);
      leftOverDistance--;
    }

    final ImmutableSet<Coordinate> targetCoordinates = targetBuilder.build();
    if (targetCoordinates.isEmpty()) {
      return Optional.absent();
    }

    return Optional.of(
        targetFactory.create(selectCoordinates, targetCoordinates));
  }
}
