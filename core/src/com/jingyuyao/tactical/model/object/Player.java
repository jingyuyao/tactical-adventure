package com.jingyuyao.tactical.model.object;

import com.jingyuyao.tactical.model.state.MapState;

/**
 * A player character
 */
public class Player extends Character {
    private boolean actionable = true;

    public Player(int x, int y, String name, Stats stats, Items items) {
        super(x, y, name, stats, items);
    }

    @Override
    public void select(MapState mapState) {
        mapState.select(this);
    }

    public boolean isActionable() {
        return actionable;
    }

    public void setActionable(boolean actionable) {
        this.actionable = actionable;
        setChanged();
        notifyObservers(new NewActionState(actionable));
    }

    public static class NewActionState {
        private final boolean actionable;

        private NewActionState(boolean actionable) {
            this.actionable = actionable;
        }

        public boolean isActionable() {
            return actionable;
        }
    }
}
