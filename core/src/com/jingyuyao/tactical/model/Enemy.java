package com.jingyuyao.tactical.model;

/**
 * An enemy character
 */
public class Enemy extends Character {
    public Enemy(int x, int y, String name, int movementDistance) {
        super(x, y, name, movementDistance);
    }

    @Override
    public void select(Selector selector) {
        selector.select(this);
    }
}
