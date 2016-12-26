package com.jingyuyao.tactical.model.object;

import com.jingyuyao.tactical.model.state.MapState;

/**
 * A player character
 */
public class Player extends Character {
    private boolean actionable = true;
    private TargetMode targetMode = TargetMode.NONE;

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
        notifyObservers(new ActionableChange(actionable));
    }

    public TargetMode getTargetMode() {
        return targetMode;
    }

    public void setTargetMode(TargetMode targetMode) {
        this.targetMode = targetMode;
        setChanged();
        notifyObservers(new TargetModeChange());
    }

    @Override
    protected void died() {
        setTargetMode(TargetMode.NONE);
    }

    public enum TargetMode {
        NONE,
        MOVE_AND_TARGETS,
        IMMEDIATE_TARGETS
    }

    public static class ActionableChange {
        private final boolean actionable;

        private ActionableChange(boolean actionable) {
            this.actionable = actionable;
        }

        public boolean isActionable() {
            return actionable;
        }
    }

    public static class TargetModeChange {
        private TargetModeChange() {}
    }
}
