package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.Marking;
import com.jingyuyao.tactical.model.MarkingFactory;
import com.jingyuyao.tactical.model.TargetInfo;
import com.jingyuyao.tactical.model.object.Character;
import com.jingyuyao.tactical.model.object.Enemy;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * Contains all the markings in a state.
 */
public class StateMarkings implements Observer {
    private final MarkingFactory markingFactory;
    private final Map<Enemy, Marking> dangerAreas;
    private Marking playerMarking = Marking.EMPTY;

    StateMarkings(MarkingFactory markingFactory) {
        this.markingFactory = markingFactory;
        dangerAreas = new HashMap<Enemy, Marking>();
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

    // TODO: danger area needs to follow enemy as it moves around, maybe make an update method
    // that is called after every state change?
    void toggleDangerArea(Enemy enemy, TargetInfo targetInfo) {
        if (dangerAreas.containsKey(enemy)) {
            Marking marking = dangerAreas.remove(enemy);
            marking.clear();
        } else {
            Marking dangerArea = markingFactory.danger(targetInfo);
            dangerAreas.put(enemy, dangerArea);
            enemy.addObserver(this);
            dangerArea.apply();
        }
    }

    @Override
    public void update(Observable object, Object param) {
        if (Character.Died.class.isInstance(param) && Enemy.class.isInstance(object)) {
            Enemy enemy = Enemy.class.cast(object);
            Marking dangerMarking = dangerAreas.remove(enemy);
            if (dangerMarking != null) {
                dangerMarking.clear();
            }
        }
    }
}
