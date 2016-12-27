package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.Coordinate;
import com.jingyuyao.tactical.model.Marker;
import com.jingyuyao.tactical.model.Waiter;
import com.jingyuyao.tactical.model.object.Character;

import java.util.Map;
import java.util.Observable;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class CharacterActor<T extends Character> extends BaseActor<T> {
    private static final float TIME_PER_UNIT = 0.06f; // time to move across one world unit in seconds

    private final Sprite sprite;

    CharacterActor(
            T object,
            float size,
            Waiter waiter,
            Map<Marker, Sprite> markerSpriteMap,
            Sprite sprite,
            Color tint,
            EventListener listener
    ) {
        super(object, size, waiter, markerSpriteMap, listener);
        this.sprite = sprite;
        sprite.setColor(tint);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (sprite != null) {
            sprite.setBounds(getX(), getY(), getWidth(), getHeight());
            sprite.draw(batch);
        }
        super.draw(batch, parentAlpha);
    }

    @Override
    public void update(Observable observable, Object param) {
        if (Character.Move.class.isInstance(param)) {
            moveTo(Character.Move.class.cast(param));
        } else if (Character.InstantMove.class.isInstance(param)) {
            instantMoveTo(Character.InstantMove.class.cast(param));
        } else if (Character.Died.class.isInstance(param)) {
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
                getWaiter().finishOne();
            }
        }));
        getWaiter().waitOne();
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
