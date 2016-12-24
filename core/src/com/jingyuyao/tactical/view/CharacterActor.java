package com.jingyuyao.tactical.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.jingyuyao.tactical.model.AnimationCounter;
import com.jingyuyao.tactical.model.Character;
import com.jingyuyao.tactical.model.Coordinate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Observable;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class CharacterActor<T extends Character> extends MapActor<T> {
    private static final float TIME_PER_UNIT = 0.1f; // time to move across one world unit in seconds

    private final Sprite sprite;

    CharacterActor(
            T object,
            float size,
            AnimationCounter animationCounter,
            Sprite sprite,
            Color tint,
            EventListener listener
    ) {
        super(object, size, animationCounter, listener);
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
        Character character = (Character) observable;
        if (o != null && o instanceof Character.PositionUpdate) {
            updatePosition(character, (Character.PositionUpdate) o);
        }
        updateDeath(character);
    }

    Sprite getSprite() {
        return sprite;
    }

    private void updateDeath(Character character) {
        if (character.isDead()) {
            // TODO: Do we have to deference to avoid memory leak?
            remove();
        }
    }

    private void updatePosition(Character character, Character.PositionUpdate positionUpdate) {
        if (getX() != character.getCoordinate().getX() || getY() != character.getCoordinate().getY()) {
            final Collection<EventListener> listeners = popAllListeners();
            SequenceAction moveSequence = getMoveSequence(positionUpdate.getPath());
            moveSequence.addAction(run(new Runnable() {
                @Override
                public void run() {
                    for (EventListener listener : listeners) {
                        addListener(listener);
                    }
                    getAnimationCounter().finishOneAnimation();
                }
            }));
            getAnimationCounter().startOneAnimation();
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
