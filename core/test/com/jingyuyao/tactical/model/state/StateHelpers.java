package com.jingyuyao.tactical.model.state;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class StateHelpers {

  static void verifyBack(Action action, MapState mapState) {
    action.run();
    verify(mapState).pop();
    verifyNoMoreInteractions(mapState);
  }
}
