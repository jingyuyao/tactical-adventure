package com.jingyuyao.tactical.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.AnimationCounter;
import com.jingyuyao.tactical.model.Character;
import com.jingyuyao.tactical.model.Coordinate;

import java.util.Observable;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class CharacterActor<T extends Character> extends MapActor<T> {
    private static final float TIME_PER_UNIT = 0.06f; // time to move across one world unit in seconds

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
        if (Character.Move.class.isInstance(o)) {
            moveTo((Character.Move) o);
        } else if (Character.InstantMove.class.isInstance(o)) {
            instantMoveTo((Character.InstantMove) o);
        } else if (Character.Dead.class.isInstance(o)) {
            remove();
        }
    }

    Sprite getSprite() {
        return sprite;
    }

    private void instantMoveTo(Character.InstantMove instantMove) {
        Coordinate destination = instantMove.getDestination();
        setPosition(destination.getX(), destination.getY());
    }

    private void moveTo(Character.Move move) {
        final ImmutableList<EventListener> listeners = popAllListeners();
        SequenceAction moveSequence = getMoveSequence(move.getPath());
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

    private SequenceAction getMoveSequence(Iterable<Coordinate> path) {
        SequenceAction sequence = sequence();
        for (Coordinate terrain : path) {
            sequence.addAction(Actions.moveTo(terrain.getX(), terrain.getY(), TIME_PER_UNIT));
        }
        return sequence;
    }

    private ImmutableList<EventListener> popAllListeners() {
        ImmutableList.Builder<EventListener> builder = new ImmutableList.Builder<EventListener>();
        for (EventListener listener : getListeners()) {
            builder.add(listener);
        }
        getListeners().clear();
        return builder.build();
    }
}
