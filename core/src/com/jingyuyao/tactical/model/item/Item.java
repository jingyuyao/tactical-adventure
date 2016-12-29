package com.jingyuyao.tactical.model.item;

import com.google.common.eventbus.EventBus;

/**
 * Super class of all the items in the game.
 */
public class Item {
    private final EventBus eventBus;
    private final int id;
    private final String name;

    public Item(EventBus eventBus, int id, String name) {
        this.eventBus = eventBus;
        this.id = id;
        this.name = name;
    }

    protected EventBus getEventBus() {
        return eventBus;
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
