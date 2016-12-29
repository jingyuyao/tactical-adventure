package com.jingyuyao.tactical.model.state;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.Marking;
import com.jingyuyao.tactical.model.MarkingFactory;
import com.jingyuyao.tactical.model.TargetInfo;
import com.jingyuyao.tactical.model.object.Character;
import com.jingyuyao.tactical.model.object.Enemy;
import com.jingyuyao.tactical.model.util.Disposed;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains all the markings in a state.
 */
public class Markings {
    private final MarkingFactory markingFactory;
    private final Map<Character, Marking> dangerAreas;
    private Marking playerMarking = Marking.EMPTY;

    Markings(EventBus eventBus, MarkingFactory markingFactory) {
        this.markingFactory = markingFactory;
        dangerAreas = new HashMap<Character, Marking>();
        eventBus.register(this);
    }

    void showMoveAndTargets(TargetInfo targetInfo) {
        playerMarking.clear();
        playerMarking = markingFactory.moveAndTargets(targetInfo);
        playerMarking.apply();
    }

    void showImmediateTargets(TargetInfo targetInfo) {
        playerMarking.clear();
        playerMarking = markingFactory.immediateTargets(targetInfo);
        playerMarking.apply();
    }

    void clearPlayerMarking() {
        playerMarking.clear();
        playerMarking = Marking.EMPTY;
    }

    // TODO: bugged, needs to be refreshed after every state
    void toggleDangerArea(TargetInfo targetInfo) {
        Character enemy = targetInfo.getCharacter();
        if (dangerAreas.containsKey(enemy)) {
            Marking marking = dangerAreas.remove(enemy);
            marking.clear();
        } else {
            Marking dangerArea = markingFactory.danger(targetInfo);
            dangerAreas.put(enemy, dangerArea);
            dangerArea.apply();
        }
    }

    @Subscribe
    public void characterDeath(Disposed disposed) {
        if (disposed.isOfClass(Enemy.class)) {
            dangerAreas.remove(disposed.getObjectAs(Enemy.class));
        }
    }
}
