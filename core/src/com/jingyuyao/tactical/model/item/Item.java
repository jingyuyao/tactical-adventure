package com.jingyuyao.tactical.model.item;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.util.DisposableObject;

/**
 * Super class of all the items in the game.
 */
public class Item extends DisposableObject {
    private final int id;
    private final String name;

    Item(EventBus eventBus, int id, String name) {
        super(eventBus);
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
