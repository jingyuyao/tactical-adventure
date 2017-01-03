package com.jingyuyao.tactical.model.common;

/**
 * For objects that can be removed from the model (e.g. characters & items).
 */
public interface Disposable {
    /**
     * Run any necessary clean up code to remove this object from the model or reset it to a clean state.
     */
    void dispose();
}
