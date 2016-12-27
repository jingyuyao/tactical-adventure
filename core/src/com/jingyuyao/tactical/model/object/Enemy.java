package com.jingyuyao.tactical.model.object;

import com.jingyuyao.tactical.model.Marking;
import com.jingyuyao.tactical.model.state.MapState;

/**
 * An enemy character
 */
public class Enemy extends Character {
    private Marking dangerMarking;

    public Enemy(int x, int y, String name, Stats stats, Items items) {
        super(x, y, name, stats, items);
    }

    @Override
    public void select(MapState mapState) {
        mapState.select(this);
    }

    public void toggleDangerMarking(Marking marking) {
        if (dangerMarking == null) {
            dangerMarking = marking;
            dangerMarking.apply();
        } else {
            dangerMarking.clear();
            dangerMarking = null;
        }
    }
}
