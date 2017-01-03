package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.common.ModelEvent;

public interface Action extends ModelEvent {
    String getName();

    void run();
}
