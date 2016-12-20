package com.jingyuyao.tactical.model;

/**
 * A player character
 */
public class Player extends Character {
    public Player(int x, int y, String name, int movementDistance) {
        super(x, y, name, movementDistance);
    }

    @Override
    public void select(Selector selector) {
        selector.select(this);
    }
}
