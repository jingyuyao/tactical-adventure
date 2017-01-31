package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.map.TerrainGraphs;
import javax.inject.Inject;

public class Waiting extends AbstractState {

  private final Characters characters;
  private final TerrainGraphs terrainGraphs;

  @Inject
  Waiting(
      MapState mapState,
      StateFactory stateFactory,
      Characters characters,
      TerrainGraphs terrainGraphs) {
    super(mapState, stateFactory);
    this.characters = characters;
    this.terrainGraphs = terrainGraphs;
  }

  @Override
  public void select(Player player) {
    if (player.isActionable()) {
      goTo(getStateFactory().createMoving(player, terrainGraphs.distanceFrom(player)));
    }
  }

  @Override
  public ImmutableList<Action> getActions() {
    return ImmutableList.<Action>of(this.new EndTurn());
  }

  public class EndTurn implements Action {

    @Override
    public String getText() {
      return "end";
    }

    @Override
    public void run() {
      for (Player player : characters.getPlayers()) {
        player.setActionable(true);
      }
      goTo(getStateFactory().createRetaliating());
    }
  }
}
