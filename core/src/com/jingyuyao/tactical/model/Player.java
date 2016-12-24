package com.jingyuyao.tactical.model;

import com.jingyuyao.tactical.model.state.MapState;

import java.util.List;
import java.util.Set;

/**
 * A player character
 */
public class Player extends Character {
    public Player(int x, int y, String name, int movementDistance, Set<Terrain.Type> canCrossTerrainTypes, List<Weapon> weapons) {
        super(x, y, name, movementDistance, canCrossTerrainTypes, weapons);
    }

    @Override
    public void select(MapState mapState) {
        mapState.select(this);
    }

    void setActionable(boolean actionable) {
        setChanged();
        notifyObservers(new ActionableChange(actionable));
    }

    public static class ActionableChange {
        private final boolean actionable;

        ActionableChange(boolean actionable) {
            this.actionable = actionable;
        }

        public boolean isActionable() {
            return actionable;
        }
    }
}
