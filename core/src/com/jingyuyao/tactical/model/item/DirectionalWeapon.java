package com.jingyuyao.tactical.model.item;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.map.Directions;
import com.jingyuyao.tactical.model.map.Terrains;
import javax.inject.Inject;

/**
 * A weapon that can be targeted in all directions in {@link Directions#ALL}.
 */
// TODO: test me
public class DirectionalWeapon extends AbstractWeapon {

  private transient final Terrains terrains;
  private transient final TargetFactory targetFactory;
  private int distance;

  @Inject
  DirectionalWeapon(Characters characters, Terrains terrains, TargetFactory targetFactory) {
    super(characters);
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
    int leftOverDistance = distance;

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
