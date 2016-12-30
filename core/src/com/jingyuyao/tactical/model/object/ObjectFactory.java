package com.jingyuyao.tactical.model.object;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.Coordinate;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.mark.Marker;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class ObjectFactory {
    private final EventBus eventBus;
    private final Provider<List<Marker>> markersProvider;

    @Inject
    ObjectFactory(EventBus eventBus, @MapObject.InitialMarkers Provider<List<Marker>> markersProvider) {
        this.eventBus = eventBus;
        this.markersProvider = markersProvider;
    }

    public Player createPlayer(int x, int y, String name, Stats stats, Items items) {
        return new Player(eventBus, new Coordinate(x, y), markersProvider.get(), name, stats, items);
    }

    public Enemy createEnemy(int x, int y, String name, Stats stats, Items items) {
        return new Enemy(eventBus, new Coordinate(x, y), markersProvider.get(), name, stats, items);
    }

    public Items createItems(List<Weapon> weapons, List<Consumable> consumables) {
        return new Items(eventBus, weapons, consumables);
    }
}
