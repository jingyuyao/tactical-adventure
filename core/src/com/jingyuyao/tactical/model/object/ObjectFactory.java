package com.jingyuyao.tactical.model.object;

import com.google.common.eventbus.EventBus;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ObjectFactory {
    private final EventBus eventBus;

    @Inject
    ObjectFactory(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public Player createPlayer(int x, int y, String name, Stats stats, Items items) {
        return new Player(eventBus, x, y, name, stats, items);
    }

    public Enemy createEnemy(int x, int y, String name, Stats stats, Items items) {
        return new Enemy(eventBus, x, y, name, stats, items);
    }

    public Terrain createTerrain(int x, int y, Terrain.Type type) {
        return new Terrain(eventBus, x, y, type);
    }
}
