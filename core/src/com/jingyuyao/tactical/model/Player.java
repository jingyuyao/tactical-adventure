package com.jingyuyao.tactical.model;

import com.jingyuyao.tactical.model.state.MapState;

/**
 * A player character
 */
public class Player extends Character {
    private boolean actionable = false;

    public Player(int x, int y, String name, int movementDistance) {
        super(x, y, name, movementDistance);
    }

    @Override
    public void select(MapState mapState) {
        mapState.select(this);
    }

    public boolean isActionable() {
        return actionable;
    }

    void setActionable(boolean actionable) {
        this.actionable = actionable;
        setChanged();
        notifyObservers();
    }
}
