package com.jingyuyao.tactical.model;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.object.AbstractObject;
import com.jingyuyao.tactical.model.object.Character;
import com.jingyuyao.tactical.model.util.Disposed;
import com.jingyuyao.tactical.model.util.EventObject;

import java.util.Map;

/**
 * A marking is a map of {@link AbstractObject} to {@link Marker}.
 * It is tied to a {@link Character} {@link #owner} and cleared upon death.
 */
public class Marking extends EventObject {
    private final Character owner;
    private final Map<AbstractObject, Marker> markers;
    private final Waiter waiter;
    private boolean applied = false;
    private boolean cleared = false;

    /**
     * Creates a marking with the given {@code markers} map and attempts to apply them immediately
     * when {@code waiter} is not waiting.
     */
    Marking(EventBus eventBus, Character owner, Map<AbstractObject, Marker> markers, Waiter waiter) {
        super(eventBus);
        this.owner = owner;
        this.markers = markers;
        this.waiter = waiter;
    }

    @Subscribe
    public void characterDeath(Disposed<Character> disposed) {
        if (disposed.matches(owner)) {
            clear();
        }
    }

    /**
     * Applies this marking if it has not been applied and {@link #clear()} has not been called.
     * Also observes {@link #owner}'s death to clear itself.
     */
    public void apply() {
        waiter.runOnce(new Runnable() {
            @Override
            public void run() {
                if (cleared || applied) return;

                for (Map.Entry<AbstractObject, Marker> entry : markers.entrySet()) {
                    entry.getKey().addMarker(entry.getValue());
                }
                applied = true;
            }
        });
    }

    /**
     * Clears the {@link #markers} held by this marking and prevent further {@link #apply()}.
     */
    public void clear() {
        waiter.runOnce(new Runnable() {
            @Override
            public void run() {
                if (cleared) return;
                // Set flag immediately so apply() would not get called for the rare cases where
                // clear() is called before apply()
                cleared = true;

                if (!applied) return;

                for (Map.Entry<AbstractObject, Marker> entry : markers.entrySet()) {
                    entry.getKey().removeMarker(entry.getValue());
                }
                getEventBus().unregister(Marking.this);
            }
        });
    }
}
