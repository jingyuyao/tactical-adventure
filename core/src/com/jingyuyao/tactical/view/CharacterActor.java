package com.jingyuyao.tactical.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.jingyuyao.tactical.model.Character;
import com.jingyuyao.tactical.model.Terrain;
import com.jingyuyao.tactical.model.UpdateListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class CharacterActor extends MapActor<Character> {
    private static final float TIME_PER_UNIT = 0.1f; // time to move across one world unit in seconds

    private final java.util.Map<Character.Type, Color> typeColorMap;
    private final Sprite sprite;

    CharacterActor(Character object, float size, Sprite sprite, Map<Character.Type, Color> typeColorMap,
                   EventListener... listeners) {
        super(object, size, listeners);
        this.sprite = sprite;
        this.typeColorMap = typeColorMap;
        updateType();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (sprite != null) {
            sprite.setBounds(getX(), getY(), getWidth(), getHeight());
            sprite.draw(batch);
        }
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

    private void updateType() {
        sprite.setColor(typeColorMap.get(getObject().getType()));
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

    private SequenceAction getMoveSequence(Collection<Terrain> path) {
        SequenceAction sequence = sequence();
        for (Terrain terrain : path) {
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
