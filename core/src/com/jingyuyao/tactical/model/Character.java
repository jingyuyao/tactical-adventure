package com.jingyuyao.tactical.model;

public class Character extends MapObject {
    /**
     * Used for sprites and as ID.
     */
    private final String name;

    public Character(String name, int x, int y) {
        super(x, y);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
