package com.jingyuyao.tactical.model.state;

public interface Action {

  String getName();

  /**
   * Perform the action. <br/> Implementation should be simple method references. All complexity
   * should be contained in the {@link State} this action originate from.
   */
  void run();
}
