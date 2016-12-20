package com.jingyuyao.tactical.model;

import java.util.Observable;

/**
 * Manages highlight logic.
 * Keep this class dumb since highlights only occurs for desktop.
 */
public class Highlighter extends Observable {
    private MapObject highlight;

    public MapObject getHighlight() {
        return highlight;
    }

    public void setHighlight(MapObject newHighlight) {
        highlight = newHighlight;
        setChanged();
        notifyObservers();
    }
}
