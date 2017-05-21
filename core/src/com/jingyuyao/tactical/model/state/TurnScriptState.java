package com.jingyuyao.tactical.model.state;

import com.google.common.base.Preconditions;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.script.TurnEvent;
import com.jingyuyao.tactical.model.state.Turn.TurnStage;
import com.jingyuyao.tactical.model.world.World;

/**
 * A state that triggers any turn scripts upon entering. Note: only scripts for {@link
 * TurnStage#START} and {@link TurnStage#END} are allowed.
 */
abstract class TurnScriptState extends BaseState {

  private final World world;
  private final ScriptRunner scriptRunner;

  TurnScriptState(
      ModelBus modelBus, WorldState worldState, World world, ScriptRunner scriptRunner) {
    super(modelBus, worldState);
    this.world = world;
    this.scriptRunner = scriptRunner;
  }

  @Override
  public void enter() {
    Turn turn = getTurn();
    TurnStage turnStage = turn.getStage();
    Preconditions.checkState(turnStage.equals(TurnStage.START) || turnStage.equals(TurnStage.END));
    super.enter();
    TurnEvent turnEvent = new TurnEvent(turn, world);
    scriptRunner.triggerScripts(turnEvent, getScript())
        .done(new Runnable() {
          @Override
          public void run() {
            scriptDone();
          }
        });
  }

  World getWorld() {
    return world;
  }

  /**
   * Called when all the script stuff finished executing. Branch away here.
   */
  abstract void scriptDone();
}
