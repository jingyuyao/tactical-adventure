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
   * Can {@link #character} move to {@code to}.
   */
  public boolean canMoveTo(Coordinate to) {
    return moves().contains(to);
  }

  /**
   * All the move coordinates {@link #character}'s current location.
   */
  public ImmutableSet<Coordinate> moves() {
    return ImmutableSet.copyOf(moveMap.keys());
  }

  /**
   * Get a path to a {@link Coordinate} from {@link #moves()}.
   */
  public Path pathTo(Coordinate to) {
    return new Path(to, algorithms.getTrackTo(moveGraph, to));
  }

  /**
   * Get the optimal path to {@code target}. Optimal path is the path where the destination has the
   * greatest number of weapon choices to hit target. <br>
   * Preconditions: {@code allTargets().contains(target)}
   */
  public Path movePathToTarget(Coordinate target) {
    Preconditions.checkArgument(all().targets().contains(target));

    Coordinate currentBestTerrain = null;
    int currentMaxWeapons = 0;
    for (Coordinate move : moveMap.keys()) {
      ImmutableSet<Weapon> weaponsForMove = weaponsFor(move, target);
      if (weaponsForMove.size() > currentMaxWeapons) {
        currentMaxWeapons = weaponsForMove.size();
        currentBestTerrain = move;
      }
    }

    Preconditions.checkNotNull(currentBestTerrain);
    return pathTo(currentBestTerrain);
  }

  /**
   * Return all the weapons that can hit {@code to} if {@link #character} stand on {@code from}.
   */
  public ImmutableSet<Weapon> weaponsFor(Coordinate from, Coordinate to) {
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
    public ImmutableSet<Coordinate> targets() {
      return ImmutableSet.copyOf(targetCoordinates);
    }

    /**
     * Return all the target coordinates from {@link #character}'s current position given this
     * filter but excluding the move coordinates.
     */
    public ImmutableSet<Coordinate> targetsMinusMove() {
      return ImmutableSet.copyOf(Sets.difference(targetCoordinates, moves()));
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
