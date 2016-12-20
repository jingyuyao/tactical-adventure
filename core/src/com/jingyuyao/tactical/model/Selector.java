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
    /**
     * State transition should be handled by methods like {@link #goToWaitingState()}
     */
    private State state;

    Selector(Map map) {
        this.map = map;
        selectedEnemies = new ArrayList<Character>();
        goToWaitingState();
    }

    public void select(Character character) {
        // By using return state, we can let IDE check if we are missing any branches ;)
        switch (state) {
            case WAITING:
                handleWaiting(character);
                break;
            case MOVING:
                handleMoving(character);
                break;
            case TARGETING:
                handleTargeting(character);
                break;
        }
        syncTerrainMarkers();
    }

    private void handleWaiting(Character character) {
        switch (character.getType()) {
            case PLAYER:
                goToMovingState(character);
                break;
            case ENEMY:
                if (selectedEnemies.contains(character)) {
                    selectedEnemies.remove(character);
                } else {
                    selectedEnemies.add(character);
                }
                goToWaitingState();
                break;
        }
    }

    private void handleMoving(Character character) {
        switch (character.getType()) {
            case PLAYER:
                if (lastSelectedPlayer.equals(character)) {
                    goToWaitingState();
                } else {
                    goToMovingState(character);
                }
                break;
            case ENEMY:
                Collection<Terrain> attackTerrains = getAttackTerrains(lastSelectedPlayer);
                Terrain enemyTerrain = map.get(character.getX(), character.getY());
                if (attackTerrains.contains(enemyTerrain)) {
                    // TODO: Move character & enter battle prep
                    goToWaitingState();
                } else {
                    goToWaitingState();
                }
                break;
        }
    }

    private void handleTargeting(Character character) {
        switch (character.getType()) {
            case PLAYER:
                // TODO: cancel?
                goToWaitingState();
                break;
            case ENEMY:
                // TODO: enter battle prep
                goToBattlePrepState(character);
                break;
        }
    }

    public void select(Terrain terrain) {
        switch (state) {
            case WAITING:
                // Do nothing
                break;
            case MOVING:
                moveIfAble(lastSelectedPlayer, terrain);
                // TODO: check to go to action state or targeting state
                goToWaitingState();
                syncTerrainMarkers();
                break;
            case TARGETING:
                goToWaitingState();
                syncTerrainMarkers();
                break;
        }
    }

    private void goToMovingState(Character playerCharacter) {
        lastSelectedPlayer = playerCharacter;
        state = State.MOVING;
    }

    private void goToWaitingState() {
        lastSelectedPlayer = null;
        state = State.WAITING;
    }

    private void goToTargetingState(Character playerCharacter) {
        // TODO: finish me
        state = State.TARGETING;
    }

    private void goToBattlePrepState(Character target) {
        state = State.BATTLE_PREP;
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
        BATTLE_PREP,
    }
}
