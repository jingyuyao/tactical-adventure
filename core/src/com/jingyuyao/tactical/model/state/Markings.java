package com.jingyuyao.tactical.model.state;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.BindingAnnotation;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.event.CharacterDied;
import com.jingyuyao.tactical.model.map.TargetInfoFactory;
import com.jingyuyao.tactical.model.mark.Marking;
import com.jingyuyao.tactical.model.mark.MarkingFactory;
import com.jingyuyao.tactical.model.util.Disposable;
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
public class Markings extends EventObject implements Disposable {
    private final MarkingFactory markingFactory;
    private final TargetInfoFactory targetInfoFactory;
    private final Map<Character, Marking> dangerAreas;
    private Marking playerMarking;

    @Inject
    public Markings(
            EventBus eventBus,
            MarkingFactory markingFactory,
            TargetInfoFactory targetInfoFactory,
            @InitialDangerAreas Map<Character, Marking> dangerAreas
    ) {
        super(eventBus);
        this.markingFactory = markingFactory;
        this.targetInfoFactory = targetInfoFactory;
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

    void showMoveAndTargets(Player player) {
        clearPlayerMarking();
        playerMarking = markingFactory.moveAndTargets(player.createTargetInfo());
        playerMarking.apply();
    }

    void showImmediateTargets(Player player) {
        clearPlayerMarking();
        playerMarking = markingFactory.immediateTargets(player.createTargetInfo());
        playerMarking.apply();
    }

    void clearPlayerMarking() {
        if (playerMarking != null) {
            playerMarking.clear();
            playerMarking = null;
        }
    }

    // TODO: bugged, needs to be refreshed after every state
    void toggleDangerArea(Enemy enemy) {
        if (dangerAreas.containsKey(enemy)) {
            Marking marking = dangerAreas.remove(enemy);
            marking.clear();
        } else {
            Marking dangerArea = markingFactory.danger(targetInfoFactory.create(enemy));
            dangerAreas.put(enemy, dangerArea);
            dangerArea.apply();
        }
    }

    @BindingAnnotation @Target({FIELD, PARAMETER, METHOD}) @Retention(RUNTIME)
    @interface InitialDangerAreas {}
}
