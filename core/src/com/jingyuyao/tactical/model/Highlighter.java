package com.jingyuyao.tactical.model;

/**
 * Manages highlight logic.
 * Keep this class dumb since highlights only occurs for desktop.
 */
public class Highlighter extends Updatable {
    private MapObject highlight;

    public MapObject getHighlight() {
        return highlight;
    }

    public void setHighlight(MapObject newHighlight) {
        highlight = newHighlight;
        update();
    }
}
