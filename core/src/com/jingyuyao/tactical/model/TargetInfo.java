package com.jingyuyao.tactical.model;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.*;
import com.google.common.graph.Graph;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.object.AbstractObject;
import com.jingyuyao.tactical.model.object.Character;

import java.util.Set;

/**
 * A snapshot of all the things a character currently can target or move to on the map.
 */
// TODO: needs to be thoroughly tested
public class TargetInfo {
    private final Map map;
    private final Character character;
    private final Graph<Coordinate> moveGraph;
    /**
     * Map of move coordinate to maps of target coordinates to weapons for that target.
     * Like so:
     *                                  move
     *                              /    ...     \
     *                          target          target
     *                      /   ...   \       /  ...   \
     *                   weapon     weapon  weapon    weapon
     */
    private final SetMultimap<Coordinate, SetMultimap<Coordinate, Weapon>> moveMap;

    private TargetInfo(
            Map map,
            Character character,
            Graph<Coordinate> moveGraph,
            SetMultimap<Coordinate, SetMultimap<Coordinate, Weapon>> moveMap) {
        this.map = map;
        this.character = character;
        this.moveGraph = moveGraph;
        this.moveMap = moveMap;
    }

    public Character getCharacter() {
        return character;
    }

    /**
     * Can {@code target} be hit after moving?
     */
    public boolean canHitAfterMove(Character target) {
        return allTargets().contains(target.getCoordinate());
    }

    /**
     * Can {@code target} be hit without moving?
     */
    public boolean canHitImmediately(Character target) {
        return immediateTargets().contains(target.getCoordinate());
    }

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
    public ImmutableList<Coordinate> pathTo(Coordinate to) {
        Preconditions.checkArgument(moveMap.keys().contains(to));
        return Algorithms.findPathTo(moveGraph, to);
    }

    /**
     * Return the {@link Coordinate} with the greatest number of {@link Weapon} choices for target.
     */
    public Coordinate moveForTarget(Coordinate target) {
        Preconditions.checkArgument(allTargets().contains(target));

        Coordinate currentBestTerrain = null;
        int currentMaxWeapons = 0;
        for (Coordinate move : moveMap.keys()) {
            ImmutableSet<Weapon> weaponsForMove = weaponsFor(move, target);
            if (weaponsForMove.size() > currentMaxWeapons) {
                currentMaxWeapons = weaponsForMove.size();
                currentBestTerrain = move;
            }
        }
        return currentBestTerrain;
    }

    /**
     * Return all the target coordinates from {@link #character}'s current position after moving.
     */
    public ImmutableSet<Coordinate> allTargets() {
        return ImmutableSet.copyOf(
                Iterables.concat(
                        Iterables.transform(
                                moveMap.values(),
                                new MultiMapKeysExtractor())));
    }

    /**
     * Return all the target coordinates from {@link #character}'s current position without moving.
     */
    public ImmutableSet<Coordinate> immediateTargets() {
        return ImmutableSet.copyOf(
                Iterables.concat(
                        Iterables.transform(
                                moveMap.get(character.getCoordinate()),
                                new MultiMapKeysExtractor())));
    }

    /**
     * Return all the target coordinates from {@link #character}'s current position after moving but
     * excluding the move coordinates.
     */
    public ImmutableSet<Coordinate> allTargetsMinusMove() {
        return ImmutableSet.copyOf(Sets.difference(allTargets(), moves()));
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
     * Return all the {@link Character} that can be targeted by {@link #character} after moving.
     */
    public ImmutableList<Character> allTargetCharacters() {
        return ImmutableList.copyOf(Iterables.filter(
                map.getCharacters(),
                Predicates.and(
                        new ContainsCoordinatePredicate(allTargets()),
                        new CanTargetPredicate(character))));
    }

    /**
     * Return all the {@link Character} that can be targeted by {@link #character} without moving.
     */
    public ImmutableList<Character> immediateTargetCharacters() {
        return ImmutableList.copyOf(Iterables.filter(
                map.getCharacters(),
                Predicates.and(
                        new ContainsCoordinatePredicate(immediateTargets()),
                        new CanTargetPredicate(character))));
    }

    /**
     * Returns {@link SetMultimap#keys()} from the inputs.
     */
    private static class MultiMapKeysExtractor implements Function<SetMultimap<Coordinate, ?>, Iterable<Coordinate>> {
        @Override
        public Iterable<Coordinate> apply(SetMultimap<Coordinate, ?> map) {
            return map.keys();
        }
    }

    /**
     * Predicate for whether the input {@link Coordinate} belongs in a {@link Set}.
     */
    private static class ContainsCoordinatePredicate implements Predicate<AbstractObject> {
        private final Set<Coordinate> coordinates;

        private ContainsCoordinatePredicate(Set<Coordinate> coordinates) {
            this.coordinates = coordinates;
        }

        @Override
        public boolean apply(AbstractObject object) {
            return coordinates.contains(object.getCoordinate());
        }
    }

    /**
     * Predicate for whether the input {@link Character} can be targeted by an attacking {@link Character}.
     */
    private static class CanTargetPredicate implements Predicate<Character> {
        private final Character attackingCharacter;

        private CanTargetPredicate(Character attackingCharacter) {
            this.attackingCharacter = attackingCharacter;
        }

        @Override
        public boolean apply(Character other) {
            return attackingCharacter.canTarget(other);
        }
    }

    public static class Factory {
        private final Map map;

        public Factory(Map map) {
            this.map = map;
        }

        /**
         * Magic.
         */
        public TargetInfo create(Character character) {
            Graph<Coordinate> moveGraph = map.getMoveGraph(character);
            SetMultimap<Coordinate, SetMultimap<Coordinate, Weapon>> moveMap = HashMultimap.create();
            for (Coordinate move : moveGraph.nodes()) {
                SetMultimap<Coordinate, Weapon> targetWeaponMap = HashMultimap.create();
                for (Weapon weapon : character.getWeapons()) {
                    // TODO: we need to be smarter if we want irregular weapon target areas
                    // we also needs a different class of target indicators for user targetable weapons
                    for (int distance : weapon.getAttackDistances()) {
                        for (Coordinate target : map.getTerrains().getNDistanceAway(move, distance)) {
                            targetWeaponMap.put(target, weapon);
                        }
                    }
                }
                moveMap.put(move, targetWeaponMap);
            }
            return new TargetInfo(map, character, moveGraph, moveMap);
        }
    }
}