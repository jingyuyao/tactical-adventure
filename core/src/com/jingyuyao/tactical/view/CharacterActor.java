package com.jingyuyao.tactical.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.jingyuyao.tactical.model.Character;
import com.jingyuyao.tactical.model.Coordinate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Observable;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class CharacterActor extends MapActor<Character> {
    private static final float TIME_PER_UNIT = 0.1f; // time to move across one world unit in seconds

    private final Sprite sprite;

    CharacterActor(
            Character object,
            float size,
            Sprite sprite,
            Color tint,
            EventListener listener
    ) {
        super(object, size, listener);
        this.sprite = sprite;
        sprite.setColor(tint);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (sprite != null) {
            sprite.setBounds(getX(), getY(), getWidth(), getHeight());
            sprite.draw(batch);
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        updatePosition();
        updateDeath();
    }

    private void updateDeath() {
        if (getObject().isDead()) {
            // TODO: Do we have to deference to avoid memory leak?
            remove();
        }
    }

    private void updatePosition() {
        if (getX() != getObject().getCoordinate().getX() || getY() != getObject().getCoordinate().getY()) {
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

    private SequenceAction getMoveSequence(Collection<Coordinate> path) {
        SequenceAction sequence = sequence();
        for (Coordinate terrain : path) {
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
