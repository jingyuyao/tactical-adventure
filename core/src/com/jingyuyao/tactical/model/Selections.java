package com.jingyuyao.tactical.model;

import com.google.common.base.Objects;
import com.google.common.graph.Graph;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Contains selected objects on the map and how to display map markers.
 */
// TODO: This class needs to be thoroughly tested
class Selections {
    private final Map map;
    /**
     * We will highlight all the danger areas of the selection enemies.
     */
    private final Collection<Character> selectedEnemies;
    /**
     * Can be null.
     */
    private Character selectedPlayer;

    Selections(Map map) {
        this.map = map;
        selectedEnemies = new ArrayList<Character>();
    }

    Character getSelectedPlayer() {
        return selectedPlayer;
    }

    void selectedPlayer(Character player) {
        if (Objects.equal(selectedPlayer, player)) {
            selectedPlayer = null;
        } else {
            selectedPlayer = player;
        }
        syncTerrainMarkers();
    }

    void selectedEnemy(Character enemy) {
        if (selectedEnemies.contains(enemy)) {
            selectedEnemies.remove(enemy);
        } else {
            selectedEnemies.add(enemy);
        }
        syncTerrainMarkers();
    }

    private void syncTerrainMarkers() {
        clearAllMarkers();
        for (Character character : selectedEnemies) {
            Collection<Terrain> dangerTerrains = map.getAttackTerrains(character);
            for (Terrain terrain : dangerTerrains) {
                terrain.addMarker(Terrain.Marker.DANGER);
            }
        }

        if (selectedPlayer != null) {
            Graph<Terrain> moveGraph = map.getMoveGraph(selectedPlayer);
            Collection<Terrain> attackTerrains = map.getAttackTerrains(selectedPlayer);

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

    private void clearAllMarkers() {
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                map.get(x, y).clearMarkers();
            }
        }
    }
}
