package com.jingyuyao.tactical.model;

import com.jingyuyao.tactical.model.state.MapState;

/**
 * Bundles all objects for a level.
 */
public class Level {
    private final Map map;
    private final MapState mapState;
    private final Turn turn;
    private final AnimationCounter animationCounter;

    public Level(Map map, MapState mapState, Turn turn, AnimationCounter animationCounter) {
        this.map = map;
        this.mapState = mapState;
        this.turn = turn;
        this.animationCounter = animationCounter;
    }

    public Map getMap() {
        return map;
    }

    public MapState getMapState() {
        return mapState;
    }

    public Turn getTurn() {
        return turn;
    }

    public AnimationCounter getAnimationCounter() {
        return animationCounter;
    }
}
