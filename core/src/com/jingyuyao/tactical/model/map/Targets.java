package com.jingyuyao.tactical.model.map;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import com.google.common.collect.SetMultimap;
import com.google.common.graph.Graph;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.common.Algorithms;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.item.Weapon;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

/**
 * A snapshot of all the things a character currently can target or move to on the map.
 */
public class Targets {

  private final Algorithms algorithms;
  private final Characters characters;
  private final Terrains terrains;
  private final Character character;
  private final Graph<Coordinate> moveGraph;
  /**
   * Map of move coordinate to multi-map of target coordinates to weapons.
   */
  private final Map<Coordinate, SetMultimap<Coordinate, Weapon>> moveMap;
  private final FilteredTargets all;
  private final FilteredTargets immediate;

  Targets(
      Algorithms algorithms,
      Characters characters,
      Terrains terrains,
      Character character,
      Graph<Coordinate> moveGraph,
      Map<Coordinate, SetMultimap<Coordinate, Weapon>> moveMap) {
    // Make sure moveGraph and moveMap contains the same move coordinates
    Preconditions.checkArgument(moveGraph.nodes().equals(moveMap.keySet()));

    this.algorithms = algorithms;
    this.characters = characters;
    this.terrains = terrains;
    this.character = character;
    this.moveGraph = moveGraph;
    this.moveMap = moveMap;
    this.all = new FilteredTargets(extractAllTargetCoordinates(moveMap.values()));
    this.immediate = new FilteredTargets(moveMap.get(character.getCoordinate()).keySet());
  }

  private static Iterable<Coordinate> extractAllTargetCoordinates(
      Collection<SetMultimap<Coordinate, Weapon>> multiMaps) {
    return
        Iterables.concat(
            Iterables.transform(
                multiMaps,
                new Function<Multimap<Coordinate, ?>, Iterable<Coordinate>>() {
                  @Override
                  public Iterable<Coordinate> apply(Multimap<Coordinate, ?> input) {
                    return input.keySet();
                  }
                }));
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
    return moveMap.keySet().contains(coordinate);
  }

  /**
   * All the move {@link Coordinate} {@link #character}'s current location.
   */
  public ImmutableSet<Coordinate> moveCoordinates() {
    return ImmutableSet.copyOf(moveMap.keySet());
  }

  /**
   * Get the {@link Terrain}s this target can move to.
   */
  public Iterable<Terrain> moveTerrains() {
    return terrains.getAll(moveMap.keySet());
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
  public Path movePathToTargetCoordinate(final Coordinate targetCoordinate) {
    Preconditions.checkArgument(all().coordinates().contains(targetCoordinate));

    Coordinate bestTerrain = Collections.max(moveMap.keySet(), new Comparator<Coordinate>() {
      @Override
      public int compare(Coordinate t1, Coordinate t2) {
        // TODO: prefer the coordinate that is closer to origin
        int numWeaponsForT1 = availableWeapons(t1, targetCoordinate).size();
        int numWeaponsForT2 = availableWeapons(t2, targetCoordinate).size();
        return numWeaponsForT1 - numWeaponsForT2;
      }
    });
    return pathTo(bestTerrain);
  }

  /**
   * Return all the {@link Weapon} that can hit {@code to} if {@link #character} were to stand on
   * {@code from}.
   */
  public ImmutableSet<Weapon> availableWeapons(Coordinate from, Coordinate to) {
    Preconditions.checkArgument(moveMap.containsKey(from));

    // No need to check `from` contains `to` as SetMultimap return an empty collection if a key
    // does not exist
    return ImmutableSet.copyOf(moveMap.get(from).get(to));
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
     * Return the {@link Terrain}s this filtered view can target.
     */
    public Iterable<Terrain> terrains() {
      return terrains.getAll(targetCoordinates);
    }

    /**
     * Return all the {@link Character} that can be targeted by {@link #character} with this filter.
     */
    public ImmutableList<Character> characters() {
      return
          ImmutableList.copyOf(
              Iterables.filter(
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
