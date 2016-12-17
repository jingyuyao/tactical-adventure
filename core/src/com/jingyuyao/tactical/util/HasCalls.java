package com.jingyuyao.tactical.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains a map of {@link Callable} to some key.
 * @param <T> The key type (usually an enum)
 */
public class HasCalls<T> {
    private final Map<T, Callable> callableMap;

    protected HasCalls() {
        callableMap = new HashMap<T, Callable>();
    }

    public void addCall(T key, Callable call) {
        callableMap.put(key, call);
    }

    protected void call(T key) {
        if (callableMap.containsKey(key)) {
            callableMap.get(key).call();
        }
    }
}
