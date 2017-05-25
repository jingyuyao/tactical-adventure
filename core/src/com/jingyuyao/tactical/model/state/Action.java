package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.resource.StringKey;

public interface Action {

  StringKey getText();

  /**
   * Perform the action. <br/> Implementation should be simple method references. All complexity
   * should be contained in the {@link State} this action originate from.
   */
  void run();
}
