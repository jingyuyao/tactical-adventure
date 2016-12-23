package com.jingyuyao.tactical.model.state;

import com.google.common.graph.Graph;
import com.jingyuyao.tactical.model.*;
import com.jingyuyao.tactical.model.Character;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Contains all the objects that requires markings.
 */
// TODO: This class needs to be thoroughly tested
class Markings {
    private final Map map;
    /**
     * We will highlight all the danger areas of the selection enemies.
     */
    private final Collection<Character> markedEnemies;
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

    Collection<Character> getMarkedEnemies() {
        return markedEnemies;
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
            Collection<Coordinate> dangerTargets = map.getAllTargets(enemy);
            for (Coordinate target : dangerTargets) {
                map.getTerrains().get(target).addMarker(Terrain.Marker.DANGER);
            }
        }

        if (markedPlayer != null) {
            Collection<Coordinate> targets;
            if (showImmediateTargets) {
                targets = map.getTargetsFrom(markedPlayer, markedPlayer.getCoordinate());
            } else {
                Graph<Coordinate> moveGraph = map.getMoveGraph(markedPlayer);
                for (Coordinate coordinate : moveGraph.nodes()) {
                    map.getTerrains().get(coordinate).addMarker(Terrain.Marker.MOVE);
                }
                targets = map.getAllTargets(markedPlayer);
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
