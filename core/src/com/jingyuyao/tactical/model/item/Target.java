package com.jingyuyao.tactical.model.item;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.map.Terrains;
import com.jingyuyao.tactical.model.terrain.Terrain;
import java.util.Set;
import javax.inject.Inject;

// TODO: need a method that return a "target info" for this target to be displayed
public class Target {

  private final ImmutableSet<Coordinate> selectCoordinates;
  private final ImmutableSet<Coordinate> targetCoordinates;
  private final Characters characters;
  private final Terrains terrains;

  @Inject
  Target(
      @Assisted("select") Set<Coordinate> selectCoordinates,
      @Assisted("target") Set<Coordinate> targetCoordinates,
      Characters characters,
      Terrains terrains) {
    this.selectCoordinates = ImmutableSet.copyOf(selectCoordinates);
    this.targetCoordinates = ImmutableSet.copyOf(targetCoordinates);
    this.characters = characters;
    this.terrains = terrains;
  }

  /**
   * Returns whether or not {@code coordinate} "selects" this {@link Target}.
   */
  public boolean selectedBy(Coordinate coordinate) {
    return selectCoordinates.contains(coordinate);
  }

  public boolean canTarget(Coordinate coordinate) {
    return targetCoordinates.contains(coordinate);
  }

  public FluentIterable<Character> getTargetCharacters() {
    return characters.fluent().filter(new Predicate<Character>() {
      @Override
      public boolean apply(Character input) {
        return canTarget(input.getCoordinate());
      }
    });
  }

  public Iterable<Terrain> getSelectTerrains() {
    return terrains.getAll(selectCoordinates);
  }

  public Iterable<Terrain> getTargetTerrains() {
    return terrains.getAll(targetCoordinates);
  }

  public Iterable<MapObject> getHitObjects() {
    return Iterables.concat(terrains.getAll(targetCoordinates), getTargetCharacters());
  }
}
