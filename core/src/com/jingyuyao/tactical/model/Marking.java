package com.jingyuyao.tactical.model;

import com.jingyuyao.tactical.model.object.AbstractObject;
import com.jingyuyao.tactical.model.object.Character;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * A marking is a map of {@link AbstractObject} to {@link Marker}.
 * It is tied to a {@link Character} {@link #owner} and cleared upon death.
 */
public class Marking implements Observer {
    /**
     * Used as a placeholder for a potential {@link Marking} object.
     * This instance does nothing at all.
     */
    public static final Marking EMPTY = new EmptyMarking();

    private final Character owner;
    private final Map<AbstractObject, Marker> markers;
    private final Waiter waiter;
    private boolean applied = false;
    private boolean cleared = false;

    /**
     * Creates a marking with the given {@code markers} map and attempts to apply them immediately
     * when {@code waiter} is not waiting.
     */
    Marking(Character owner, Map<AbstractObject, Marker> markers, Waiter waiter) {
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
                owner.addObserver(Marking.this);
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
                owner.deleteObserver(Marking.this);
            }
        });
    }

    @Override
    public void update(Observable observable, Object o) {
        if (Character.Died.class.isInstance(o)) {
            clear();
        }
    }

    /**
     * An empty {@link Marking} that does nothing.
     */
    private static class EmptyMarking extends Marking {
        private EmptyMarking() {
            super(null, null, null);
        }

        @Override
        public void apply() {}

        @Override
        public void clear() {}
    }
}
