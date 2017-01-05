package com.jingyuyao.tactical.model.state;

import static org.mockito.Mockito.verify;

class StateHelpers {

  static void verifyBack(Action action, MapState mapState) {
    action.run();
    verify(mapState).pop();
  }
}
