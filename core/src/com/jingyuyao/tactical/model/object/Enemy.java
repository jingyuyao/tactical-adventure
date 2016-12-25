package com.jingyuyao.tactical.model.object;

import com.jingyuyao.tactical.model.state.MapState;

/**
 * An enemy character
 */
public class Enemy extends Character {
    private boolean showDangerArea;

    public Enemy(int x, int y, String name, Stats stats, Items items) {
        super(x, y, name, stats, items);
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
