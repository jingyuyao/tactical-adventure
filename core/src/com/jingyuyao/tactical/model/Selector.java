package com.jingyuyao.tactical.model;

import com.google.common.graph.Graph;
import com.google.common.graph.ValueGraph;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Manages selection logic.
 */
// TODO: This class needs to be thoroughly tested
public class Selector {
    private final Map map;
    /**
     * We will highlight all the danger areas of the selection enemies.
     */
    private final Collection<Character> selectedEnemies;
    /**
     * Invariant: must not be null if {@link #state} == {@link State#MOVING}
     */
    private Character lastSelectedPlayer;
    private State state = State.WAITING;

    Selector(Map map) {
        this.map = map;
        selectedEnemies = new ArrayList<Character>();
    }

    public void select(Character character) {
        // By using return state, we can let IDE check if we are missing any branches ;)
        switch (state) {
            case WAITING:
                state = handleWaiting(character);
                break;
            case MOVING:
                state = handleMoving(character);
                break;
            case TARGETING:
                state = handleTargeting(character);
                break;
        }
        syncTerrainMarkers();
    }

    private State handleWaiting(Character character) {
        switch (character.getType()) {
            case PLAYER:
                lastSelectedPlayer = character;
                return State.MOVING;
            case ENEMY:
                if (selectedEnemies.contains(character)) {
                    selectedEnemies.remove(character);
                } else {
                    selectedEnemies.add(character);
                }
                return State.WAITING;
            default:
                return State.WAITING;
        }
    }

    private State handleMoving(Character character) {
        switch (character.getType()) {
            case PLAYER:
                if (lastSelectedPlayer.equals(character)) {
                    lastSelectedPlayer = null;
                    return State.WAITING;
                } else {
                    lastSelectedPlayer = character;
                    return State.MOVING;
                }
            case ENEMY:
                Collection<Terrain> attackTerrains = getAttackTerrains(lastSelectedPlayer);
                Terrain enemyTerrain = map.get(character.getX(), character.getY());
                if (attackTerrains.contains(enemyTerrain)) {
                    // TODO: Move character & enter battle prep
                    return State.MOVING;
                } else {
                    lastSelectedPlayer = null;
                    return State.WAITING;
                }
            default:
                return State.MOVING;
        }
    }

    private State handleTargeting(Character character) {
        switch (character.getType()) {
            case PLAYER:
                // TODO: cancel?
                lastSelectedPlayer = null;
                return State.WAITING;
            case ENEMY:
                // TODO: enter battle prep
                return State.TARGETING;
            default:
                return State.TARGETING;
        }
    }

    public void select(Terrain terrain) {
        switch (state) {
            case WAITING:
                // Do nothing
                break;
            case MOVING:
                moveIfAble(lastSelectedPlayer, terrain);
                lastSelectedPlayer = null;
                // TODO: check to go into attacking state?
                state = State.WAITING;
                syncTerrainMarkers();
                break;
            case TARGETING:
                lastSelectedPlayer = null;
                state = State.WAITING;
                syncTerrainMarkers();
                break;
        }
    }

    private void syncTerrainMarkers() {
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

    private enum State {
        WAITING,
        MOVING,
        TARGETING,
    }
}
