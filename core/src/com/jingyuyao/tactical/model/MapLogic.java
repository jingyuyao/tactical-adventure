package com.jingyuyao.tactical.model;

import com.google.common.base.Optional;
import com.jingyuyao.tactical.model.graph.Graph;
import com.jingyuyao.tactical.model.graph.GraphAlgorithms;
import com.jingyuyao.tactical.model.graph.Path;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Contains methods to handle when user select an object on the map.
 */
// TODO: Add the waiting to choose action phase
public class MapLogic {
    private final Map map;
    /**
     * We will highlight all movement terrains as well as attack areas.
     */
    private Character lastSelectedPlayer;
    /**
     * We will highlight all the danger areas of the selection enemies.
     */
    private Collection<Character> selectedEnemies = new ArrayList<Character>();

    MapLogic(Map map) {
        this.map = map;
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
            for (Terrain terrain : dangerGraph.getAllObjects()) {
                terrain.addMarker(Terrain.Marker.DANGER);
            }
        }

        if (lastSelectedPlayer != null) {
            // TODO: Add attack graph?
            Graph<Terrain> moveGraph = findMovePathsFor(lastSelectedPlayer);
            for (Terrain terrain : moveGraph.getAllObjects()) {
                terrain.addMarker(Terrain.Marker.MOVE);
            }
        }
    }

    private void moveIfAble(Character character, Terrain terrain) {
        int x = terrain.getX();
        int y = terrain.getY();
        Graph<Terrain> allPaths = findMovePathsFor(character);
        Optional<Path<Terrain>> pathToCoordinate = allPaths.getPathTo(x, y);
        if (pathToCoordinate.isPresent()) {
            character.moveTo(x, y, pathToCoordinate.get());
        }
    }


    private Graph<Terrain> findMovePathsFor(Character character) {
        return GraphAlgorithms.findAllPath(
                map,
                map.createEdgeCostGrid(character),
                character.getX(),
                character.getY(),
                character.getMovementDistance());
    }
}
