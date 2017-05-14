package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.script.ScriptActions;
import com.jingyuyao.tactical.model.script.TurnScript;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.World;
import javax.inject.Inject;

public class EndTurn extends ScriptState {

  private final StateFactory stateFactory;
  private final World world;

  @Inject
  EndTurn(ModelBus modelBus, WorldState worldState, StateFactory stateFactory, World world) {
    super(modelBus, worldState);
    this.stateFactory = stateFactory;
    this.world = world;
  }

  @Override
  ScriptActions getScriptActions(TurnScript turnScript) {
    return turnScript.getEnd();
  }

  @Override
  void finish() {
    makePlayersActionable();
    incrementTurn();
    branchTo(stateFactory.createRetaliating());
  }

  private void makePlayersActionable() {
    for (Cell cell : world.getCharacterSnapshot()) {
      for (Player player : cell.player().asSet()) {
        player.setActionable(true);
      }
    }
  }

}
