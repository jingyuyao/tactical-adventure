package com.jingyuyao.tactical.model;

import com.google.common.graph.Graph;

import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

public class MarkerManager implements Observer {
    private final Map map;
    private final AnimationCounter animationCounter;

    MarkerManager(Map map, AnimationCounter animationCounter) {
        this.map = map;
        this.animationCounter = animationCounter;
    }

    @Override
    public void update(Observable observable, Object o) {
        if (Player.TargetModeChange.class.isInstance(o) || Enemy.ShowDangerAreaChange.class.isInstance(o)) {
            animationCounter.runOnceWhenNotAnimating(new Runnable() {
                @Override
                public void run() {
                    syncMarkers();
                }
            });
        }
    }

    private void syncMarkers() {
        clearAllMarkers();

        for (Enemy enemy : map.getEnemies()) {
            if (enemy.isShowDangerArea()) {
                for (Coordinate target : map.getAllTargets(enemy)) {
                    map.getTerrains().get(target).addMarker(Terrain.Marker.DANGER);
                }
            }
        }

        for (Player player : map.getPlayers()) {
            Set<Coordinate> targets = new HashSet<Coordinate>();
            switch (player.getTargetMode()) {
                case MOVE_AND_TARGETS:
                    Graph<Coordinate> moveGraph = map.getMoveGraph(player);
                    for (Coordinate coordinate : moveGraph.nodes()) {
                        map.getTerrains().get(coordinate).addMarker(Terrain.Marker.MOVE);
                    }
                    targets.addAll(map.getAllTargets(player));
                    targets.removeAll(moveGraph.nodes());
                    break;
                case IMMEDIATE_TARGETS:
                    targets = map.getTargetsFrom(player, player.getCoordinate());
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
}
