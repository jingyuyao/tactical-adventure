package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.Coordinate;
import com.jingyuyao.tactical.model.Waiter;
import com.jingyuyao.tactical.model.event.Disposed;
import com.jingyuyao.tactical.model.event.InstantMove;
import com.jingyuyao.tactical.model.event.Move;
import com.jingyuyao.tactical.model.mark.Marker;
import com.jingyuyao.tactical.model.object.Character;

import java.util.Map;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

class CharacterActor<T extends Character> extends BaseActor<T> {
    private static final float TIME_PER_UNIT = 0.06f; // time to move across one world unit in seconds

    private final Sprite sprite;

    CharacterActor(
            EventBus eventBus,
            T object,
            float size,
            Waiter waiter,
            Map<Marker, Sprite> markerSpriteMap,
            Sprite sprite,
            Color tint,
            EventListener listener
    ) {
        super(eventBus, object, size, waiter, markerSpriteMap, listener);
        this.sprite = sprite;
        setColor(tint);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (sprite != null) {
            sprite.setColor(getColor());
            sprite.setBounds(getX(), getY(), getWidth(), getHeight());
            sprite.draw(batch);
        }
        super.draw(batch, parentAlpha);
    }

    @Subscribe
    public void instantMoveTo(InstantMove instantMove) {
        if (getObject().equals(instantMove.getCharacter())) {
            Coordinate destination = instantMove.getDestination();
            setPosition(destination.getX(), destination.getY());
        }
    }

    @Subscribe
    public void moveTo(Move move) {
        if (getObject().equals(move.getCharacter())) {
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
    }

    @Subscribe
    public void death(Disposed<Character> disposed) {
        if (disposed.matches(getObject())) {
            remove();
        }
    }

    Sprite getSprite() {
        return sprite;
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
