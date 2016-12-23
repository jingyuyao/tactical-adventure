package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableSet;
import com.google.common.graph.Graph;
import com.jingyuyao.tactical.model.Character;
import com.jingyuyao.tactical.model.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Contains all the objects that requires markings.
 */
// TODO: This class needs to be thoroughly tested
class Markings {
    private final Map map;
    /**
     * We will highlight all the danger areas of the selection enemies.
     */
    private final List<Character> markedEnemies;
    /**
     * Can be null.
     */
    private Player markedPlayer;
    private boolean showImmediateTargets;

    Markings(Map map) {
        this.map = map;
        markedEnemies = new ArrayList<Character>();
        showImmediateTargets = false;
    }

    ImmutableSet<Character> getMarkedEnemies() {
        return ImmutableSet.copyOf(markedEnemies);
    }

    void markPlayer(Player player, boolean immediateTargets) {
        markedPlayer = player;
        showImmediateTargets = immediateTargets;
        syncTerrainMarkers();
    }

    void unMarkLastPlayer() {
        markedPlayer = null;
        showImmediateTargets = false;
        syncTerrainMarkers();
    }

    void markEnemy(Enemy enemy) {
        markedEnemies.add(enemy);
        syncTerrainMarkers();
    }

    void unMarkEnemy(Enemy enemy) {
        markedEnemies.remove(enemy);
        syncTerrainMarkers();
    }

    void removeEnemy(Character enemy) {
        markedEnemies.remove(enemy);
        syncTerrainMarkers();
    }

    private void syncTerrainMarkers() {
        clearAllMarkers();

        for (Character enemy : markedEnemies) {
            for (Coordinate target : map.getAllTargets(enemy)) {
                map.getTerrains().get(target).addMarker(Terrain.Marker.DANGER);
            }
        }

        if (markedPlayer != null) {
            Set<Coordinate> targets;
            if (showImmediateTargets) {
                targets = map.getTargetsFrom(markedPlayer, markedPlayer.getCoordinate());
            } else {
                Graph<Coordinate> moveGraph = map.getMoveGraph(markedPlayer);
                for (Coordinate coordinate : moveGraph.nodes()) {
                    map.getTerrains().get(coordinate).addMarker(Terrain.Marker.MOVE);
                }
                targets = new HashSet<Coordinate>(map.getAllTargets(markedPlayer));
                targets.removeAll(moveGraph.nodes());
            }
            for (Coordinate target : targets) {
                map.getTerrains().get(target).addMarker(Terrain.Marker.ATTACK);
            }
        }
    }

    private void clearAllMarkers() {
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                map.getTerrains().get(x, y).clearMarkers();
            }
        }
    }
}
