package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.battle.Battle;

/**
 * Event to signal the start of a battle.
 */
public class StartBattle {

  private final Battle battle;
  private final MyFuture future;

  public StartBattle(final Battle battle, MyFuture future) {
    this.battle = battle;
    // not exposed because we always want battle to execute before future finishes.
    this.future = future;
  }

  /**
   * Do not call {@link Battle#execute()} directly, instead call {@link #start()} to complete this
   * event.
   */
  public Battle getBattle() {
    return battle;
  }

  /**
   * Called when this event has been handled by the view and the battle logic may be executed.
   * Completes the future associated with this battle.
   */
  public void start() {
    battle.execute();
    future.done();
  }
}
