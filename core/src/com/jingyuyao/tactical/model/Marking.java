package com.jingyuyao.tactical.model;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.object.AbstractObject;
import com.jingyuyao.tactical.model.object.Character;
import com.jingyuyao.tactical.model.util.Disposed;

import java.util.Map;

/**
 * A marking is a map of {@link AbstractObject} to {@link Marker}.
 * It is tied to a {@link Character} {@link #owner} and cleared upon death.
 */
public class Marking {
    /**
     * Used as a placeholder for a potential {@link Marking} object.
     * This instance does nothing at all.
     */
    public static final Marking EMPTY = new EmptyMarking();

    private final EventBus eventBus;
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
        this.eventBus = eventBus;
        this.owner = owner;
        this.markers = markers;
        this.waiter = waiter;
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
                eventBus.register(Marking.this);
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
                eventBus.unregister(Marking.this);
            }
        });
    }

    @Subscribe
    public void characterDeath(Disposed disposed) {
        if (disposed.matches(owner)) {
            clear();
        }
    }

    /**
     * An empty {@link Marking} that does nothing.
     */
    private static class EmptyMarking extends Marking {
        private EmptyMarking() {
            super(null, null, null, null);
        }

        @Override
        public void apply() {}

        @Override
        public void clear() {}
    }
}
