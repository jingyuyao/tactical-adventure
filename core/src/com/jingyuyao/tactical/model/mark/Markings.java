package com.jingyuyao.tactical.model.mark;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.BindingAnnotation;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.character.event.CharacterDied;
import com.jingyuyao.tactical.model.common.Disposable;
import com.jingyuyao.tactical.model.common.EventBusObject;
import com.jingyuyao.tactical.model.map.TargetsFactory;

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
public class Markings extends EventBusObject implements Disposable {
    private final MarkingFactory markingFactory;
    private final TargetsFactory targetsFactory;
    private final Map<Character, Marking> dangerAreas;
    private Marking playerMarking;

    @Inject
    public Markings(
            EventBus eventBus,
            MarkingFactory markingFactory,
            TargetsFactory targetsFactory,
            @InitialDangerAreas Map<Character, Marking> dangerAreas
    ) {
        super(eventBus);
        this.markingFactory = markingFactory;
        this.targetsFactory = targetsFactory;
        this.dangerAreas = dangerAreas;
        playerMarking = null;
        register();
    }

    @Override
    public void dispose() {
        clearPlayerMarking();
        for (Marking marking : dangerAreas.values()) {
            marking.clear();
        }
        dangerAreas.clear();
    }

    @Subscribe
    public void characterDied(CharacterDied characterDied) {
        dangerAreas.remove(characterDied.getObject());
    }

    public void showMoveAndTargets(Player player) {
        clearPlayerMarking();
        playerMarking = markingFactory.moveAndTargets(player.createTargetInfo());
        playerMarking.apply();
    }

    public void showImmediateTargets(Player player) {
        clearPlayerMarking();
        playerMarking = markingFactory.immediateTargets(player.createTargetInfo());
        playerMarking.apply();
    }

    public void clearPlayerMarking() {
        if (playerMarking != null) {
            playerMarking.clear();
            playerMarking = null;
        }
    }

    // TODO: bugged, needs to be refreshed after every state
    public void toggleDangerArea(Enemy enemy) {
        if (dangerAreas.containsKey(enemy)) {
            Marking marking = dangerAreas.remove(enemy);
            marking.clear();
        } else {
            Marking dangerArea = markingFactory.danger(targetsFactory.create(enemy));
            dangerAreas.put(enemy, dangerArea);
            dangerArea.apply();
        }
    }

    @BindingAnnotation @Target({FIELD, PARAMETER, METHOD}) @Retention(RUNTIME)
    @interface InitialDangerAreas {}
}
