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
            syncMarkers();
        }
    }

    private void syncMarkers() {
        map.clearAllMarkers();
        for (Character character : selectedEnemies) {
            Collection<Terrain> dangerTerrains = getAttackTerrains(character);
            for (Terrain terrain : dangerTerrains) {
                terrain.addMarker(Terrain.Marker.DANGER);
            }
        }

        if (lastSelectedPlayer != null) {
            Graph<Terrain> moveGraph = createMoveGraph(lastSelectedPlayer);
            Collection<Terrain> attackTerrains = getAttackTerrains(lastSelectedPlayer);

            for (Terrain terrain : attackTerrains) {
                if (!moveGraph.nodes().contains(terrain)) {
                    terrain.addMarker(Terrain.Marker.ATTACK);
                }
            }

            for (Terrain terrain : moveGraph.nodes()) {
                terrain.addMarker(Terrain.Marker.MOVE);
            }
        }
    }

    private void moveIfAble(Character character, Terrain terrain) {
        Graph<Terrain> pathGraph = createMoveGraph(character);
        Collection<Terrain> pathToCoordinate = Algorithms.findPathTo(pathGraph, terrain);
        if (!pathToCoordinate.isEmpty()) {
            character.moveTo(terrain.getX(), terrain.getY(), pathToCoordinate);
        }
    }

    private ValueGraph<Terrain, Integer> createMoveGraph(Character character) {
        return Algorithms.minPathSearch(
                map,
                map.createMovementPenaltyGrid(character),
                character.getX(),
                character.getY(),
                character.getMovementDistance());
    }

    private Collection<Terrain> getAttackTerrains(Character character) {
        Graph<Terrain> moveTerrains = createMoveGraph(character);
        Collection<Terrain> attackTerrains = new ArrayList<Terrain>();

        // TODO: whoa... optimize?
        for (Weapon weapon : character.getWeapons()) {
            for (int distance : weapon.getAttackDistances()) {
                for (Terrain terrain : moveTerrains.nodes()) {
                    attackTerrains.addAll(
                            Algorithms.findNDistanceAway(map, terrain.getX(), terrain.getY(), distance)
                    );
                }
            }
        }

        return attackTerrains;
    }
}
