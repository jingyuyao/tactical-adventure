package com.jingyuyao.tactical.model.item;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.EventBus;

/**
 * An {@link Item} that can be used and has a limited number of usages.
 */
public abstract class Usable extends Item {
    /**
     * Don't expose setter
     */
    private int usageLeft;

    Usable(EventBus eventBus, int id, String name, int usageLeft) {
        super(eventBus, id, name);
        Preconditions.checkArgument(usageLeft > 0);
        this.usageLeft = usageLeft;
    }

    public int getUsageLeft() {
        return usageLeft;
    }

    /**
     * Signals this item has been used once. Fires {@link Broke} when {@link #getUsageLeft()} == 0
     */
    public void useOnce() {
        Preconditions.checkState(usageLeft > 0);

        usageLeft--;
        if (usageLeft == 0) {
            getEventBus().post(new Broke(this));
        }
    }

    @Override
    public String toString() {
        return "Usable{" +
                "usageLeft=" + usageLeft +
                "} " + super.toString();
    }

    public static class Broke {
        private final Usable usable;

        private Broke(Usable usable) {
            this.usable = usable;
        }

        public Usable getUsable() {
            return usable;
        }
    }
}
