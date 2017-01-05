package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.event.ModelEvent;

public interface Action extends ModelEvent {

  String getName();

  void run();
}
