package com.jingyuyao.tactical.model;

public class Character extends MapObject {
    /**
     * Used for sprites and as ID.
     */
    private final String name;
    private int moveDistance = 3; // Hard code for now

    Character(String name, int x, int y) {
        super(x, y);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getMoveDistance() {
        return moveDistance;
    }

    public void setMoveDistance(int moveDistance) {
        this.moveDistance = moveDistance;
    }

    @Override
    public String toString() {
        return "Character{" +
                "name='" + name + '\'' +
                "} " + super.toString();
    }
}
