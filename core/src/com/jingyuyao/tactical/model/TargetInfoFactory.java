package com.jingyuyao.tactical.model;

import com.google.common.base.Function;
import com.google.common.collect.*;
import com.google.common.graph.Graph;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.object.Character;

import java.util.Collection;

public class TargetInfoFactory {
    private final Map map;

    public TargetInfoFactory(Map map) {
        this.map = map;
    }

    public TargetInfo createFor(Character character) {
        return new Builder(character).build();
    }

    private class Builder {
        private final Character character;

        private Builder(Character character) {
            this.character = character;
        }

        private TargetInfo build() {
            Graph<Coordinate> moveGraph = map.getMoveGraph(character);
            ImmutableSet<TargetInfo.Move> moves =
                    ImmutableSet.copyOf(Iterables.transform(moveGraph.nodes(), this.new CreateMove()));

            return new TargetInfo(map, character, moveGraph, moves);
        }

        private class CreateMove implements Function<Coordinate, TargetInfo.Move> {
            @Override
            public TargetInfo.Move apply(Coordinate move) {
                return new TargetInfo.Move(move, createTargets(move));
            }
        }

        private ImmutableSet<TargetInfo.Target> createTargets(Coordinate move) {
            SetMultimap<Coordinate, Weapon> coordinateWeaponMap = HashMultimap.create();
            for (Weapon weapon : character.getWeapons()) {
                // TODO: we need to be smarter if we want irregular weapon target areas
                // we also needs a different class of target indicators for user targetable weapons
                for (int distance : weapon.getAttackDistances()) {
                    for (Coordinate target : map.getTerrains().getNDistanceAway(move, distance)) {
                        coordinateWeaponMap.put(target, weapon);
                    }
                }
            }

            ImmutableSet.Builder<TargetInfo.Target> targetBuilder = new ImmutableSet.Builder<TargetInfo.Target>();
            for (java.util.Map.Entry<Coordinate, Collection<Weapon>> entry
                    : coordinateWeaponMap.asMap().entrySet()) {
                targetBuilder.add(new TargetInfo.Target(
                        entry.getKey(),
                        ImmutableSet.copyOf(entry.getValue())
                ));
            }
            return targetBuilder.build();
        }
    }
}
