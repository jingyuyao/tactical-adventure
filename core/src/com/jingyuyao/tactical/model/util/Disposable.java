package com.jingyuyao.tactical.model.util;

/**
 * For objects that can be removed from the model (e.g. characters & items).
 */
public interface Disposable {
    /**
     * Run any necessary clean up code to remove this object from the model.
     */
    void dispose();
}
