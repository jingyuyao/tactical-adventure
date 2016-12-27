package com.jingyuyao.tactical.model;

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
        Iterable<Terrain> moveTerrains = map.getTerrains().getAll(moveGraph.nodes());
        Iterable<Terrain> attackTargets =
                map.getTerrains().getAll(Sets.difference(map.getAllTargets(character), moveGraph.nodes()));

        return this.new Builder(character)
                .add(moveTerrains, Marker.MOVE)
                .add(attackTargets, Marker.ATTACK)
                .build();
    }

    public Marking immediateTargets(Character character) {
        Iterable<Terrain> attackTargets =
                map.getTerrains().getAll(map.getTargetsFrom(character, character.getCoordinate()));

        return this.new Builder(character)
                .add(attackTargets, Marker.ATTACK)
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
