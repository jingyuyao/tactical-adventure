package com.jingyuyao.tactical.model;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.graph.Graph;
import com.jingyuyao.tactical.model.object.AbstractObject;
import com.jingyuyao.tactical.model.object.Character;
import com.jingyuyao.tactical.model.object.Terrain;

/**
 * Produces {@link Marking}s
 */
public class MarkingFactory {
    private final Map map;
    private final Waiter waiter;

    public MarkingFactory(Map map, Waiter waiter) {
        this.map = map;
        this.waiter = waiter;
    }

    public Marking moveAndTargets(Character character) {
        Graph<Coordinate> moveGraph = map.getMoveGraph(character);
        Iterable<Terrain> canMoveToTerrains = map.getTerrains().getAll(moveGraph.nodes());
        Iterable<Terrain> canAttackTerrains =
                map.getTerrains().getAll(Sets.difference(map.getAllTargets(character), moveGraph.nodes()));
        // TODO: bugged, currently it shows friendly as "target" because getTargetAfterMove doesn't account for
        // canTarget()
        Iterable<Character> potentialTargets = map.getTargetAfterMoveCharacters(character);

        return this.new Builder(character)
                .add(canMoveToTerrains, Marker.CAN_MOVE_TO)
                .add(canAttackTerrains, Marker.CAN_ATTACK)
                .add(potentialTargets, Marker.POTENTIAL_TARGET)
                .build();
    }

    public Marking immediateTargets(Character character) {
        ImmutableSet<Coordinate> targetCoordinates = map.getTargetsFrom(character, character.getCoordinate());
        Iterable<Terrain> canAttackTerrains = map.getTerrains().getAll(targetCoordinates);
        Iterable<Character> potentialTargets = map.getImmediateTargetCharacters(character);

        return this.new Builder(character)
                .add(canAttackTerrains, Marker.CAN_ATTACK)
                .add(potentialTargets, Marker.POTENTIAL_TARGET)
                .build();
    }

    public Marking danger(Character character) {
        Iterable<Terrain> allTargets = map.getTerrains().getAll(map.getAllTargets(character));

        return this.new Builder(character)
                .add(allTargets, Marker.DANGER)
                .build();
    }

    private class Builder {
        private final Character character;
        private final java.util.Map<AbstractObject, Marker> markerMap;

        private Builder(Character character) {
            this.character = character;
            markerMap = new java.util.HashMap<AbstractObject,Marker>();
        }

        private Builder add(Iterable<? extends AbstractObject> objects, Marker marker) {
            for (AbstractObject object : objects) {
                markerMap.put(object, marker);
            }
            return this;
        }

        private Marking build() {
            return new Marking(character, markerMap, waiter);
        }
    }
}
