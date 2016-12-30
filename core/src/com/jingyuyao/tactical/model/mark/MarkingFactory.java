package com.jingyuyao.tactical.model.mark;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.TargetInfo;
import com.jingyuyao.tactical.model.TerrainGrid;
import com.jingyuyao.tactical.model.Waiter;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.object.Character;
import com.jingyuyao.tactical.model.object.Terrain;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Produces {@link Marking}s
 */
@Singleton
public class MarkingFactory {
    private final EventBus eventBus;
    private final Waiter waiter;
    private final TerrainGrid terrainGrid;

    @Inject
    public MarkingFactory(EventBus eventBus, Waiter waiter, TerrainGrid terrainGrid) {
        this.eventBus = eventBus;
        this.waiter = waiter;
        this.terrainGrid = terrainGrid;
    }

    public Marking moveAndTargets(TargetInfo targetInfo) {
        Iterable<Terrain> canMoveToTerrains = terrainGrid.getAll(targetInfo.moves());
        Iterable<Terrain> canAttackTerrains = terrainGrid.getAll(targetInfo.allTargetsMinusMove());
        Iterable<Character> canTargetCharacters = targetInfo.allTargetCharacters();

        return this.new Builder(targetInfo.getCharacter())
                .add(canMoveToTerrains, Marker.CAN_MOVE_TO)
                .add(canAttackTerrains, Marker.CAN_ATTACK)
                .add(canTargetCharacters, Marker.POTENTIAL_TARGET)
                .build();
    }

    public Marking immediateTargets(TargetInfo targetInfo) {
        Iterable<Terrain> canAttackTerrains = terrainGrid.getAll(targetInfo.immediateTargets());
        Iterable<Character> canTargetCharacters = targetInfo.immediateTargetCharacters();

        return this.new Builder(targetInfo.getCharacter())
                .add(canAttackTerrains, Marker.CAN_ATTACK)
                .add(canTargetCharacters, Marker.POTENTIAL_TARGET)
                .build();
    }

    public Marking danger(TargetInfo targetInfo) {
        Iterable<Terrain> allTargets = terrainGrid.getAll(targetInfo.allTargets());

        return this.new Builder(targetInfo.getCharacter())
                .add(allTargets, Marker.DANGER)
                .build();
    }

    private class Builder {
        private final Character character;
        private final java.util.Map<MapObject, Marker> markerMap;

        private Builder(Character character) {
            this.character = character;
            markerMap = new java.util.HashMap<MapObject,Marker>();
        }

        private Builder add(Iterable<? extends MapObject> objects, Marker marker) {
            for (MapObject object : objects) {
                markerMap.put(object, marker);
            }
            return this;
        }

        private Marking build() {
            return new Marking(eventBus, character, markerMap, waiter);
        }
    }
}
