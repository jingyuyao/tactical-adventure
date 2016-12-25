package com.jingyuyao.tactical.model.item;

import java.util.Observable;

/**
 * Super class of all the items in the game.
 */
public class Item extends Observable {
    private final int id;
    private final String name;

    public Item(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
