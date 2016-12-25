package com.jingyuyao.tactical.model;

import com.jingyuyao.tactical.model.state.MapState;

import java.util.List;
import java.util.Set;

/**
 * An enemy character
 */
public class Enemy extends Character {
    private boolean showDangerArea;

    public Enemy(int x, int y, String name, int movementDistance, Set<Terrain.Type> canCrossTerrainTypes, List<Weapon> weapons) {
        super(x, y, name, movementDistance, canCrossTerrainTypes, weapons);
    }

    @Override
    public void select(MapState mapState) {
        mapState.select(this);
    }

    public boolean isShowDangerArea() {
        return showDangerArea;
    }

    public void setShowDangerArea(boolean showDangerArea) {
        this.showDangerArea = showDangerArea;
        setChanged();
        notifyObservers(new ShowDangerAreaChange(showDangerArea));
    }

    public static class ShowDangerAreaChange {
        private final boolean showDangerArea;

        ShowDangerAreaChange(boolean showDangerArea) {
            this.showDangerArea = showDangerArea;
        }

        public boolean isShowDangerArea() {
            return showDangerArea;
        }
    }
}
