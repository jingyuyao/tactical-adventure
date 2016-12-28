package com.jingyuyao.tactical.model;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.google.common.graph.Graph;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.object.Character;

/**
 * A snapshot of all the things a character currently can target or move to on the map.
 */
public class TargetInfo {
    private final Map map;
    private final Character character;
    private final Graph<Coordinate> moveGraph;
    private final ImmutableSet<Move> moves;

    TargetInfo(Map map, Character character, Graph<Coordinate> moveGraph, ImmutableSet<Move> moves) {
        this.map = map;
        this.character = character;
        this.moveGraph = moveGraph;
        this.moves = moves;
    }

    public Character getCharacter() {
        return character;
    }

    public boolean canTargetAfterMove(Character target) {
        return character.canTarget(target) && allTargets().contains(target.getCoordinate());
    }

    public boolean canImmediateTarget(Character target) {
        return character.canTarget(target) && immediateTargets().contains(target.getCoordinate());
    }

    public ImmutableSet<Coordinate> moves() {
        return ImmutableSet.copyOf(Iterables.transform(moves, Move.COORDINATE_EXTRACTOR));
    }

    public ImmutableList<Coordinate> pathTo(Coordinate to) {
        return Algorithms.findPathTo(moveGraph, to);
    }

    /**
     * Return the coordinate with the greatest number of weapon choices for target.
     */
    public Coordinate moveForTarget(Coordinate target) {
        Coordinate currentBestTerrain = null;
        int currentMaxWeapons = 0;
        for (Coordinate source : Iterables.transform(moves, Move.COORDINATE_EXTRACTOR)) {
            ImmutableSet<Weapon> weaponsForThisTerrain = weaponsFor(source, target);
            if (weaponsForThisTerrain.size() > currentMaxWeapons) {
                currentMaxWeapons = weaponsForThisTerrain.size();
                currentBestTerrain = source;
            }
        }
        Preconditions.checkNotNull(currentBestTerrain);
        return currentBestTerrain;
    }

    public ImmutableSet<Coordinate> allTargets() {
        return ImmutableSet.copyOf(
                Iterables.transform(
                        Iterables.concat(
                                Iterables.transform(
                                        moves,
                                        Move.TARGETS_EXTRACTOR)),
                        Target.COORDINATE_EXTRACTOR));
    }

    public ImmutableSet<Coordinate> immediateTargets() {
        return ImmutableSet.copyOf(
                Iterables.transform(
                        originMove().getTargets(),
                        Target.COORDINATE_EXTRACTOR));
    }

    public ImmutableSet<Coordinate> allTargetsMinusMove() {
        return ImmutableSet.copyOf(Sets.difference(allTargets(), moves()));
    }

    public ImmutableSet<Weapon> weaponsFor(Coordinate from, Coordinate to) {
        Move fromMove = Iterables.find(moves, new Move.CoordinatePredicate(from));
        Preconditions.checkNotNull(fromMove);
        Optional<Target> toTarget = Iterables.tryFind(fromMove.getTargets(), new Target.CoordinatePredicate(to));
        if (toTarget.isPresent()) {
            return toTarget.get().getWeapons();
        } else {
            return ImmutableSet.of();
        }
    }

    public ImmutableList<Character> allTargetCharacters() {
        final ImmutableSet<Coordinate> allTargets = allTargets();
        return ImmutableList.copyOf(Iterables.filter(map.getCharacters(), new Predicate<Character>() {
            @Override
            public boolean apply(Character input) {
                return allTargets.contains(input.getCoordinate());
            }
        }));
    }

    public ImmutableList<Character> immediateTargetCharacters() {
        final ImmutableSet<Coordinate> immediateTargets = immediateTargets();
        return ImmutableList.copyOf(Iterables.filter(map.getCharacters(), new Predicate<Character>() {
            @Override
            public boolean apply(Character input) {
                return immediateTargets.contains(input.getCoordinate());
            }
        }));
    }

    private Move originMove() {
        return Iterables.find(moves, new Move.CoordinatePredicate(character.getCoordinate()));
    }

    // TODO: get rid of these stupid classes and use map of map instead.
    /**
     * A {@link Move} have a bunch of {@link Target} for that {@link Move}'s {@link #coordinate}.
     */
    static class Move {
        private final Coordinate coordinate;
        private final ImmutableSet<Target> targets;

        Move(Coordinate coordinate, ImmutableSet<Target> targets) {
            this.coordinate = coordinate;
            this.targets = targets;
        }

        private Coordinate getCoordinate() {
            return coordinate;
        }

        private ImmutableSet<Target> getTargets() {
            return targets;
        }

        private static final Function<Move, Coordinate> COORDINATE_EXTRACTOR = new Function<Move, Coordinate>() {
            @Override
            public Coordinate apply(Move input) {
                return input.getCoordinate();
            }
        };

        private static final Function<Move, Iterable<Target>> TARGETS_EXTRACTOR =
                new Function<Move, Iterable<Target>>() {
            @Override
            public Iterable<Target> apply(Move input) {
                return input.getTargets();
            }
        };

        private static class CoordinatePredicate implements Predicate<Move> {
            private final Coordinate target;

            private CoordinatePredicate(Coordinate target) {
                this.target = target;
            }

            @Override
            public boolean apply(Move input) {
                return input.getCoordinate().equals(target);
            }
        }
    }

    /**
     * A {@link Target} have a bunch of {@link Weapon} that can hit that {@link Target}'s {@link #coordinate}.
     */
    static class Target implements Comparable<Target> {
        private final Coordinate coordinate;
        private final ImmutableSet<Weapon> weapons;

        Target(Coordinate coordinate, ImmutableSet<Weapon> weapons) {
            this.coordinate = coordinate;
            this.weapons = weapons;
        }

        private Coordinate getCoordinate() {
            return coordinate;
        }

        private ImmutableSet<Weapon> getWeapons() {
            return weapons;
        }

        @Override
        public int compareTo(Target target) {
            return weapons.size() - target.getWeapons().size();
        }

        private static final Function<Target, Coordinate> COORDINATE_EXTRACTOR = new Function<Target, Coordinate>() {
            @Override
            public Coordinate apply(Target input) {
                return input.getCoordinate();
            }
        };

        private static class CoordinatePredicate implements Predicate<Target> {
            private final Coordinate target;

            private CoordinatePredicate(Coordinate target) {
                this.target = target;
            }

            @Override
            public boolean apply(Target input) {
                return input.getCoordinate().equals(target);
            }
        }
    }
}
