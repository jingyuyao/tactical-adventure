package com.jingyuyao.tactical.model;

import com.google.common.graph.Graph;
import com.google.common.graph.ValueGraph;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Manages selection logic.
 */
// TODO: Add the waiting to choose action phase
public class Selector {
    private final Map map;
    /**
     * We will highlight all movement terrains as well as attack areas.
     */
    private Character lastSelectedPlayer;
    /**
     * We will highlight all the danger areas of the selection enemies.
     */
    private final Collection<Character> selectedEnemies;

    Selector(Map map) {
        this.map = map;
        selectedEnemies = new ArrayList<Character>();
    }

    public void select(Character character) {
        switch (character.getType()) {
            case PLAYER:
                if (lastSelectedPlayer == character) {
                    lastSelectedPlayer = null;
                } else {
                    lastSelectedPlayer = character;
                }
                break;
            case ENEMY:
                if (lastSelectedPlayer == null) {
                    if (selectedEnemies.contains(character)) {
                        selectedEnemies.remove(character);
                    } else {
                        selectedEnemies.add(character);
                    }
                }
                // TODO: Check if enemy is standing on an attack terrain
                break;
        }
        syncMarkers();
    }

    public void select(Terrain terrain) {
        if (lastSelectedPlayer != null) {
            moveIfAble(lastSelectedPlayer, terrain);
            lastSelectedPlayer = null;
        }
        syncMarkers();
    }

    private void syncMarkers() {
        map.clearAllMarkers();
        for (Character character : selectedEnemies) {
            // TODO: Find danger graph
            Graph<Terrain> dangerGraph = findMovePathsFor(character);
            for (Terrain terrain : dangerGraph.nodes()) {
                terrain.addMarker(Terrain.Marker.DANGER);
            }
        }

        if (lastSelectedPlayer != null) {
            // TODO: Add attack graph?
            Graph<Terrain> moveGraph = findMovePathsFor(lastSelectedPlayer);
            for (Terrain terrain : moveGraph.nodes()) {
                terrain.addMarker(Terrain.Marker.MOVE);
            }
        }
    }

    private void moveIfAble(Character character, Terrain terrain) {
        int x = terrain.getX();
        int y = terrain.getY();
        Graph<Terrain> pathGraph = findMovePathsFor(character);
        Collection<Terrain> pathToCoordinate = GraphAlgorithms.findPathTo(pathGraph, x, y);
        if (!pathToCoordinate.isEmpty()) {
            character.moveTo(x, y, pathToCoordinate);
        }
    }


    private ValueGraph<Terrain, Integer> findMovePathsFor(Character character) {
        return GraphAlgorithms.createPathGraph(
                map,
                map.createEdgeCostGrid(character),
                character.getX(),
                character.getY(),
                character.getMovementDistance());
    }
}
