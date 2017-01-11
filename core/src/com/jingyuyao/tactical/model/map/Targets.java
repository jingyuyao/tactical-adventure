package com.jingyuyao.tactical.model.map;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;
import com.google.common.graph.Graph;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.common.Algorithms;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.item.Weapon;

/**
 * A snapshot of all the things a character currently can target or move to on the map.
 */
public class Targets {

  private final Algorithms algorithms;
  private final Characters characters;
  private final Character character;
  private final Graph<Coordinate> moveGraph;
  /**
   * Multi-map of move coordinate to maps of target coordinates to weapons for that target.
   */
  private final SetMultimap<Coordinate, SetMultimap<Coordinate, Weapon>> moveMap;
  private final FilteredTargets all;
  private final FilteredTargets immediate;

  Targets(
      Algorithms algorithms,
      Characters characters,
      Character character,
      Graph<Coordinate> moveGraph,
      SetMultimap<Coordinate, SetMultimap<Coordinate, Weapon>> moveMap) {
    this.algorithms = algorithms;
    this.characters = characters;
    this.character = character;
    this.moveGraph = moveGraph;
    this.moveMap = moveMap;
    this.all =
        new FilteredTargets(
            Iterables.concat(
                Iterables.transform(
                    moveMap.values(),
                    new MultiMapKeysExtractor())));
    this.immediate =
        new FilteredTargets(
            Iterables.concat(
                Iterables.transform(
                    moveMap.get(character.getCoordinate()),
                    new MultiMapKeysExtractor())));
  }

  public Character getCharacter() {
    return character;
  }

  /**
   * Return a filtered view of all the things the character can target given they could move.
   */
  public FilteredTargets all() {
    return all;
  }

  /**
   * Return a filtered view of all the things the character can target without moving.
   */
  public FilteredTargets immediate() {
    return immediate;
  }

  /**
   * Can {@link #character} move to {@code coordinate}.
   */
  public boolean canMoveTo(Coordinate coordinate) {
    return moveCoordinates().contains(coordinate);
  }

  /**
   * All the move {@link Coordinate} {@link #character}'s current location.
   */
  public ImmutableSet<Coordinate> moveCoordinates() {
    return ImmutableSet.copyOf(moveMap.keys());
  }

  /**
   * Get a path to {@code coordinate} from {@link #moveCoordinates()}.
   */
  public Path pathTo(Coordinate coordinate) {
    return new Path(coordinate, algorithms.getTrackTo(moveGraph, coordinate));
  }

  /**
   * Get the optimal path to {@code targetCoordinate}. Optimal path is the path where the
   * destination has the greatest number of weapon choices to hit target. <br> Preconditions: {@code
   * allTargets().contains(target)}
   */
  // TODO: also return the least distance traveled or prefer the origin coordinate
  public Path movePathToTargetCoordinate(Coordinate targetCoordinate) {
    Preconditions.checkArgument(all().coordinates().contains(targetCoordinate));

    Coordinate currentBestTerrain = null;
    int currentMaxWeapons = 0;
    for (Coordinate move : moveMap.keys()) {
      ImmutableSet<Weapon> weaponsForMove = availableWeapons(move, targetCoordinate);
      if (weaponsForMove.size() > currentMaxWeapons) {
        currentMaxWeapons = weaponsForMove.size();
        currentBestTerrain = move;
      }
    }

    Preconditions.checkNotNull(currentBestTerrain);
    return pathTo(currentBestTerrain);
  }

  /**
   * Return all the {@link Weapon} that can hit {@code to} if {@link #character} were to stand on
   * {@code from}.
   */
  public ImmutableSet<Weapon> availableWeapons(Coordinate from, Coordinate to) {
    Preconditions.checkArgument(moveMap.containsKey(from));

    for (SetMultimap<Coordinate, Weapon> fromTargets : moveMap.get(from)) {
      if (fromTargets.containsKey(to)) {
        return ImmutableSet.copyOf(fromTargets.get(to));
      }
    }
    return ImmutableSet.of();
  }

  /**
   * Returns {@link SetMultimap#keys()} from the inputs.
   */
  private static class MultiMapKeysExtractor
      implements Function<SetMultimap<Coordinate, ?>, Iterable<Coordinate>> {

    @Override
    public Iterable<Coordinate> apply(SetMultimap<Coordinate, ?> map) {
      return map.keys();
    }
  }

  /**
   * A filtered view of the target {@link Coordinate}s.
   */
  public class FilteredTargets {

    private final ImmutableSet<Coordinate> targetCoordinates;

    private FilteredTargets(Iterable<Coordinate> targetCoordinates) {
      this.targetCoordinates = ImmutableSet.copyOf(targetCoordinates);
    }

    /**
     * Can {@code target} be hit with this filter.
     */
    public boolean canTarget(Character target) {
      return character.canTarget(target) && targetCoordinates.contains(target.getCoordinate());
    }

    /**
     * Return all the target coordinates from {@link #character}'s current position given this
     * filter.
     */
    public ImmutableSet<Coordinate> coordinates() {
      return ImmutableSet.copyOf(targetCoordinates);
    }

    /**
     * Return all the target coordinates from {@link #character}'s current position given this
     * filter but excluding the move coordinates.
     */
    public ImmutableSet<Coordinate> targetsMinusMove() {
      return ImmutableSet.copyOf(Sets.difference(targetCoordinates, moveCoordinates()));
    }

    /**
     * Return all the {@link Character} that can be targeted by {@link #character} with this filter.
     */
    public ImmutableList<Character> characters() {
      return ImmutableList.copyOf(Iterables.filter(
          characters,
          new Predicate<Character>() {
            @Override
            public boolean apply(Character other) {
              return canTarget(other);
            }
          }));
    }
  }
}
