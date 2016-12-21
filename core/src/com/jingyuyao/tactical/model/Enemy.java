package com.jingyuyao.tactical.model;

import com.jingyuyao.tactical.model.state.MapState;

/**
 * An enemy character
 */
public class Enemy extends Character {
    public Enemy(int x, int y, String name, int movementDistance) {
        super(x, y, name, movementDistance);
    }

    @Override
    public void select(MapState mapState) {
        mapState.select(this);
    }
}
