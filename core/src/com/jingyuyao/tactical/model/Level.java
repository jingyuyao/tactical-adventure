package com.jingyuyao.tactical.model;

import com.jingyuyao.tactical.model.state.MapState;

/**
 * Bundles all objects for a level.
 */
public class Level {
    private final Map map;
    private final MapState mapState;
    private final Turn turn;
    private final Highlighter highlighter;
    private final Waiter waiter;

    public Level(Map map, MapState mapState, Turn turn, Highlighter highlighter, Waiter waiter) {
        this.map = map;
        this.mapState = mapState;
        this.turn = turn;
        this.highlighter = highlighter;
        this.waiter = waiter;
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

    public Highlighter getHighlighter() {
        return highlighter;
    }

    public Waiter getWaiter() {
        return waiter;
    }
}
