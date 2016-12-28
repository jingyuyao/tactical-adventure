package com.jingyuyao.tactical.model;

import com.jingyuyao.tactical.model.object.AbstractObject;
import com.jingyuyao.tactical.model.object.Character;
import com.jingyuyao.tactical.model.object.Terrain;

/**
 * Produces {@link Marking}s
 */
public class MarkingFactory {
    private final Map map;
    private final Waiter waiter;

    public MarkingFactory(Map map, Waiter waiter) {
        this.map = map;
        this.waiter = waiter;
    }

    public Marking moveAndTargets(TargetInfo targetInfo) {
        Iterable<Terrain> canMoveToTerrains = map.getTerrains().getAll(targetInfo.moves());
        Iterable<Terrain> canAttackTerrains = map.getTerrains().getAll(targetInfo.allTargetsMinusMove());
        Iterable<Character> canTargetCharacters = targetInfo.allTargetCharacters();

        return this.new Builder(targetInfo.getCharacter())
                .add(canMoveToTerrains, Marker.CAN_MOVE_TO)
                .add(canAttackTerrains, Marker.CAN_ATTACK)
                .add(canTargetCharacters, Marker.POTENTIAL_TARGET)
                .build();
    }

    public Marking immediateTargets(TargetInfo targetInfo) {
        Iterable<Terrain> canAttackTerrains = map.getTerrains().getAll(targetInfo.immediateTargets());
        Iterable<Character> canTargetCharacters = targetInfo.immediateTargetCharacters();

        return this.new Builder(targetInfo.getCharacter())
                .add(canAttackTerrains, Marker.CAN_ATTACK)
                .add(canTargetCharacters, Marker.POTENTIAL_TARGET)
                .build();
    }

    public Marking danger(TargetInfo targetInfo) {
        Iterable<Terrain> allTargets = map.getTerrains().getAll(targetInfo.allTargets());

        return this.new Builder(targetInfo.getCharacter())
                .add(allTargets, Marker.DANGER)
                .build();
    }

    private class Builder {
        private final Character character;
        private final java.util.Map<AbstractObject, Marker> markerMap;

        private Builder(Character character) {
            this.character = character;
            markerMap = new java.util.HashMap<AbstractObject,Marker>();
        }

        private Builder add(Iterable<? extends AbstractObject> objects, Marker marker) {
            for (AbstractObject object : objects) {
                markerMap.put(object, marker);
            }
            return this;
        }

        private Marking build() {
            return new Marking(character, markerMap, waiter);
        }
    }
}
