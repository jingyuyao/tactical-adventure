package com.jingyuyao.tactical.model.state;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.BindingAnnotation;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.event.CharacterDied;
import com.jingyuyao.tactical.model.map.TargetInfo;
import com.jingyuyao.tactical.model.mark.Marking;
import com.jingyuyao.tactical.model.mark.MarkingFactory;
import com.jingyuyao.tactical.model.util.EventObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Map;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Contains all the markings in a state.
 */
@Singleton
public class Markings extends EventObject {
    private final MarkingFactory markingFactory;
    private final Map<Character, Marking> dangerAreas;
    private Marking playerMarking;

    @Inject
    public Markings(EventBus eventBus, MarkingFactory markingFactory, @InitialDangerAreas Map<Character, Marking> dangerAreas) {
        super(eventBus);
        this.markingFactory = markingFactory;
        this.dangerAreas = dangerAreas;
        playerMarking = null;
        register();
    }

    @Override
    public void dispose() {
        dangerAreas.clear();
        playerMarking = null;
        super.dispose();
    }

    @Subscribe
    public void characterDied(CharacterDied characterDied) {
        dangerAreas.remove(characterDied.getObject());
    }

    void showMoveAndTargets(TargetInfo targetInfo) {
        clearPlayerMarking();
        playerMarking = markingFactory.moveAndTargets(targetInfo);
        playerMarking.apply();
    }

    void showImmediateTargets(TargetInfo targetInfo) {
        clearPlayerMarking();
        playerMarking = markingFactory.immediateTargets(targetInfo);
        playerMarking.apply();
    }

    void clearPlayerMarking() {
        if (playerMarking != null) {
            playerMarking.clear();
            playerMarking = null;
        }
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

    @BindingAnnotation @Target({FIELD, PARAMETER, METHOD}) @Retention(RUNTIME)
    @interface InitialDangerAreas {}
}
