package com.jingyuyao.tactical.model.state;

public interface Action {
    String getName();

    Runnable getRunnable();
}
