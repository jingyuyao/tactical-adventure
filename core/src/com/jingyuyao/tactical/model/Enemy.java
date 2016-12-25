package com.jingyuyao.tactical.model;

import com.jingyuyao.tactical.model.item.Items;
import com.jingyuyao.tactical.model.state.MapState;

import java.util.Set;

/**
 * An enemy character
 */
public class Enemy extends Character {
    private boolean showDangerArea;

    public Enemy(int x, int y, String name, int movementDistance, Set<Terrain.Type> canCrossTerrainTypes, Items items) {
        super(x, y, name, movementDistance, canCrossTerrainTypes, items);
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
        notifyObservers(new ShowDangerAreaChange());
    }

    public static class ShowDangerAreaChange {
        ShowDangerAreaChange() {}
    }
}
