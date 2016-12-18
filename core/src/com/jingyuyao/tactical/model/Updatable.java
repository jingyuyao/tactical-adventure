package com.jingyuyao.tactical.model;

/**
 * Accepts an {@link UpdateListener} to be notified about changes to this object.
 * Children should manage when to call {@link #update()}.
 */
abstract class Updatable {
    private UpdateListener updateListener;

    public void setUpdateListener(UpdateListener updateListener) {
        this.updateListener = updateListener;
    }

    /**
     * Alerts {@link UpdateListener} for this object that updates have been made if it exists.
     */
    void update() {
        if (updateListener != null) {
            updateListener.updated();
        }
    }
}
