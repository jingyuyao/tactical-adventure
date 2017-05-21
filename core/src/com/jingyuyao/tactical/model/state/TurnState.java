package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.script.TurnEvent;
import com.jingyuyao.tactical.model.world.World;

/**
 * A state that triggers any turn transition scripts upon entering.
 */
abstract class TurnState extends BaseState {

  private final World world;
  private final ScriptRunner scriptRunner;

  TurnState(ModelBus modelBus, WorldState worldState, World world, ScriptRunner scriptRunner) {
    super(modelBus, worldState);
    this.world = world;
    this.scriptRunner = scriptRunner;
  }

  @Override
  public void enter() {
    super.enter();
    TurnEvent turnEvent = new TurnEvent(getTurn(), world);
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
