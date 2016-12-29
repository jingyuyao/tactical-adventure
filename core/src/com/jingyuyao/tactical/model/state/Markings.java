package com.jingyuyao.tactical.model.state;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.Marking;
import com.jingyuyao.tactical.model.MarkingFactory;
import com.jingyuyao.tactical.model.TargetInfo;
import com.jingyuyao.tactical.model.object.Character;
import com.jingyuyao.tactical.model.object.Enemy;
import com.jingyuyao.tactical.model.util.DisposableObject;
import com.jingyuyao.tactical.model.util.Disposed;

import java.util.Map;

/**
 * Contains all the markings in a state.
 */
public class Markings extends DisposableObject {
    private final MarkingFactory markingFactory;
    private final Map<Character, Marking> dangerAreas;
    private Marking playerMarking;

    public Markings(EventBus eventBus, MarkingFactory markingFactory, Map<Character, Marking> dangerAreas) {
        super(eventBus);
        this.markingFactory = markingFactory;
        this.dangerAreas = dangerAreas;
        playerMarking = Marking.EMPTY;
    }

    @Override
    protected void disposed() {
        dangerAreas.clear();
        playerMarking = Marking.EMPTY;
        super.disposed();
    }

    @Subscribe
    public void characterDeath(Disposed<Enemy> disposed) {
        dangerAreas.remove(disposed.getObject());
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
}
