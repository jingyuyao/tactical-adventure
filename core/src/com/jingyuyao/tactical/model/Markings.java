package com.jingyuyao.tactical.model;

import com.google.common.collect.Sets;
import com.google.common.graph.Graph;
import com.jingyuyao.tactical.model.object.Character;
import com.jingyuyao.tactical.model.object.Terrain;

import java.util.Collections;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

// TODO: change this to something like CharacterMarkers
public class Markings implements Observer {
    private final Map map;
    private final Waiter waiter;

    Markings(Map map, Waiter waiter) {
        this.map = map;
        this.waiter = waiter;
    }

    @Override
    public void update(final Observable object, final Object param) {
        if (Character.MarkerModeChange.class.isInstance(param)) {
            waiter.runOnceWhenNotWaiting(new Runnable() {
                @Override
                public void run() {
                    targetModeChange(Character.class.cast(object), Character.MarkerModeChange.class.cast(param));
                }
            });
        }
    }

    private void targetModeChange(Character character, Character.MarkerModeChange markerModeChange) {
        Grid<Terrain> terrains = map.getTerrains();
        java.util.Map<Coordinate, Marker> terrainMarkers = character.getTerrainMarkers();
        Set<Coordinate> attackTargets = Collections.emptySet();

        switch (markerModeChange.getNewMarkerMode()) {
            case NONE:
                for (java.util.Map.Entry<Coordinate, Marker> entry : terrainMarkers.entrySet()) {
                    Terrain terrain = terrains.get(entry.getKey());
                    terrain.removeMarker(entry.getValue());
                }
                terrainMarkers.clear();
                break;
            case MOVE_AND_TARGETS:
                Graph<Coordinate> moveGraph = map.getMoveGraph(character);
                for (Coordinate coordinate : moveGraph.nodes()) {
                    terrains.get(coordinate).addMarker(Marker.MOVE);
                    terrainMarkers.put(coordinate, Marker.MOVE);
                }
                attackTargets = Sets.difference(map.getAllTargets(character), moveGraph.nodes());
                break;
            case IMMEDIATE_TARGETS:
                attackTargets = map.getTargetsFrom(character, character.getCoordinate());
                break;
            case DANGER:
                for (Coordinate target : map.getAllTargets(character)) {
                    terrains.get(target).addMarker(Marker.DANGER);
                    terrainMarkers.put(target, Marker.DANGER);
                }
                break;
        }

        for (Coordinate target : attackTargets) {
            terrains.get(target).addMarker(Marker.ATTACK);
            terrainMarkers.put(target, Marker.ATTACK);
        }
    }
}
