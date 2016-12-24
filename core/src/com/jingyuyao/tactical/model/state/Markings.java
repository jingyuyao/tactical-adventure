package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableSet;
import com.google.common.graph.Graph;
import com.jingyuyao.tactical.model.*;
import com.jingyuyao.tactical.model.Character;

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
    private final AnimationCounter animationCounter;
    /**
     * We will highlight all the danger areas of the selection enemies.
     */
    private final List<Character> markedEnemies;
    /**
     * Can be null.
     */
    private Player markedPlayer;
    private PlayerTargetMode playerTargetMode;
    private Enemy targetEnemy;

    Markings(Map map, AnimationCounter animationCounter) {
        this.map = map;
        this.animationCounter = animationCounter;
        markedEnemies = new ArrayList<Character>();
        playerTargetMode = PlayerTargetMode.MOVE_AND_TARGETS;
    }

    ImmutableSet<Character> getMarkedEnemies() {
        return ImmutableSet.copyOf(markedEnemies);
    }

    void markMoveAndTargets(Player player) {
        markedPlayer = player;
        playerTargetMode = PlayerTargetMode.MOVE_AND_TARGETS;
        syncMarkersOnAnimationComplete();
    }

    void markImmediateTargets(Player player) {
        markedPlayer = player;
        playerTargetMode = PlayerTargetMode.IMMEDIATE_TARGETS;
        syncMarkersOnAnimationComplete();
    }

    void markEnemyTarget(Player player, Enemy enemy) {
        markedPlayer = player;
        playerTargetMode = PlayerTargetMode.TARGET_ENEMY;
        targetEnemy = enemy;
        syncMarkersOnAnimationComplete();
    }

    void unMarkLastPlayer() {
        markedPlayer = null;
        syncMarkers();
    }

    void markEnemyDangerArea(Enemy enemy) {
        markedEnemies.add(enemy);
        syncMarkersOnAnimationComplete();
    }

    void unMarkEnemyDangerArea(Enemy enemy) {
        markedEnemies.remove(enemy);
        syncMarkers();
    }

    private void syncMarkersOnAnimationComplete() {
        animationCounter.runOnceWhenNotAnimating(new Runnable() {
            @Override
            public void run() {
                syncMarkers();
            }
        });
    }

    private void syncMarkers() {
        clearAllMarkers();

        for (Character enemy : markedEnemies) {
            for (Coordinate target : map.getAllTargets(enemy)) {
                map.getTerrains().get(target).addMarker(Terrain.Marker.DANGER);
            }
        }

        if (markedPlayer != null) {
            Set<Coordinate> targets = new HashSet<Coordinate>();
            switch (playerTargetMode) {
                case MOVE_AND_TARGETS:
                    Graph<Coordinate> moveGraph = map.getMoveGraph(markedPlayer);
                    for (Coordinate coordinate : moveGraph.nodes()) {
                        map.getTerrains().get(coordinate).addMarker(Terrain.Marker.MOVE);
                    }
                    targets.addAll(map.getAllTargets(markedPlayer));
                    targets.removeAll(moveGraph.nodes());
                    break;
                case IMMEDIATE_TARGETS:
                    targets = map.getTargetsFrom(markedPlayer, markedPlayer.getCoordinate());
                    break;
                case TARGET_ENEMY:
                    targets.add(targetEnemy.getCoordinate());
                    break;
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

    private enum PlayerTargetMode {
        MOVE_AND_TARGETS,
        IMMEDIATE_TARGETS,
        TARGET_ENEMY
    }
}
