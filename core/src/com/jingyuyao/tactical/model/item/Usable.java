package com.jingyuyao.tactical.model.item;

import com.google.common.base.Preconditions;

/**
 * An {@link Item} that can be used and has a limited number of usages.
 */
public abstract class Usable extends Item {
    /**
     * Don't expose setter
     */
    private int usageLeft;

    public Usable(int id, String name, int usageLeft) {
        super(id, name);
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
            setChanged();
            notifyObservers(new Broke());
            // TODO: we can get leave out this if usable can be refilled
            deleteObservers();
        }
    }

    @Override
    public String toString() {
        return "Usable{" +
                "usageLeft=" + usageLeft +
                "} " + super.toString();
    }

    public static class Broke {
        private Broke() {}
    }
}
