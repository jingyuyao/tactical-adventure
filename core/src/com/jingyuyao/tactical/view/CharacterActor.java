package com.jingyuyao.tactical.view;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.jingyuyao.tactical.model.Character;
import com.jingyuyao.tactical.model.Terrain;
import com.jingyuyao.tactical.model.UpdateListener;
import com.jingyuyao.tactical.model.graph.Path;

import java.util.ArrayList;
import java.util.Collection;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class CharacterActor extends MapActor<Character> {
    private static final float TIME_PER_UNIT = 0.1f; // time to move across one world unit in seconds

    CharacterActor(Character object, float size, Sprite sprite, EventListener... listeners) {
        super(object, size, sprite, listeners);
    }

    @Override
    protected UpdateListener getUpdateListener() {
        return new UpdateListener() {
            @Override
            public void updated() {
                updatePosition();
            }
        };
    }

    private void updatePosition() {
        if (getX() != getObject().getX() || getY() != getObject().getY()) {
            final Collection<EventListener> listeners = popAllListeners();
            SequenceAction moveSequence = getMoveSequence(getObject().getLastPath());
            moveSequence.addAction(run(new Runnable() {
                @Override
                public void run() {
                    for (EventListener listener : listeners) {
                        addListener(listener);
                    }
                }
            }));
            addAction(moveSequence);
        }
    }

    private SequenceAction getMoveSequence(Path<Terrain> path) {
        SequenceAction sequence = sequence();
        for (Terrain terrain : path.getRoute()) {
            sequence.addAction(moveTo(terrain.getX(), terrain.getY(), TIME_PER_UNIT));
        }
        return sequence;
    }

    private Collection<EventListener> popAllListeners() {
        Collection<EventListener> listeners = new ArrayList<EventListener>();
        for (EventListener listener : getListeners()) {
            listeners.add(listener);
        }
        getListeners().clear();
        return listeners;
    }
}
