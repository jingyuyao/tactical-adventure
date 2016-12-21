package com.jingyuyao.tactical.model;

import com.google.common.base.Objects;
import com.google.common.graph.Graph;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Contains selected objects on the map and how to display map markers.
 */
// TODO: This class needs to be thoroughly tested
public class Selections {
    private final Map map;
    /**
     * We will highlight all the danger areas of the selection enemies.
     */
    private final Collection<Character> selectedEnemies;
    /**
     * Can be null.
     */
    private Character selectedPlayer;
    private boolean showImmediateTargets;

    Selections(Map map) {
        this.map = map;
        selectedEnemies = new ArrayList<Character>();
        showImmediateTargets = false;
    }

    public Character getSelectedPlayer() {
        return selectedPlayer;
    }

    public void selectedPlayer(Character player) {
        if (Objects.equal(selectedPlayer, player)) {
            selectedPlayer = null;
        } else {
            selectedPlayer = player;
        }
        showImmediateTargets = false;
        syncTerrainMarkers();
    }

    public void selectedEnemy(Character enemy) {
        if (selectedEnemies.contains(enemy)) {
            selectedEnemies.remove(enemy);
        } else {
            selectedEnemies.add(enemy);
        }
        syncTerrainMarkers();
    }

    public void removeEnemy(Character enemy) {
        selectedEnemies.remove(enemy);
        syncTerrainMarkers();
    }

    public void showImmediateTargets() {
        showImmediateTargets = true;
        syncTerrainMarkers();
    }

    private void syncTerrainMarkers() {
        clearAllMarkers();

        for (Character character : selectedEnemies) {
            Collection<Terrain> dangerTerrains = map.getAllTargetTerrains(character);
            for (Terrain terrain : dangerTerrains) {
                terrain.addMarker(Terrain.Marker.DANGER);
            }
        }

        if (selectedPlayer != null) {
            Collection<Terrain> targetTerrains;
            if (showImmediateTargets) {
                targetTerrains =
                        map.getTargetTerrains(selectedPlayer, map.getTerrain(selectedPlayer.getX(), selectedPlayer.getY()));
            } else {
                Graph<Terrain> moveGraph = map.getMoveGraph(selectedPlayer);
                for (Terrain terrain : moveGraph.nodes()) {
                    terrain.addMarker(Terrain.Marker.MOVE);
                }
                targetTerrains = map.getAllTargetTerrains(selectedPlayer);
                targetTerrains.removeAll(moveGraph.nodes());
            }
            for (Terrain terrain : targetTerrains) {
                terrain.addMarker(Terrain.Marker.ATTACK);
            }
        }
    }

    private void clearAllMarkers() {
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                map.getTerrain(x, y).clearMarkers();
            }
        }
    }
}
