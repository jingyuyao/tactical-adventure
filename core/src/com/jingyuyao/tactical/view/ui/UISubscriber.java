package com.jingyuyao.tactical.view.ui;

import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.event.SelectCell;
import com.jingyuyao.tactical.model.event.WorldReset;
import com.jingyuyao.tactical.model.state.Battling;
import com.jingyuyao.tactical.model.state.PlayerState;
import com.jingyuyao.tactical.model.state.SelectingTarget;
import com.jingyuyao.tactical.model.state.State;
import com.jingyuyao.tactical.model.state.UsingConsumable;
import com.jingyuyao.tactical.model.world.Cell;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UISubscriber {

  private final ActionGroup actionGroup;
  private final CharacterPanel characterPanel;
  private final TerrainPanel terrainPanel;
  private final ItemPanel itemPanel;

  @Inject
  UISubscriber(
      ActionGroup actionGroup, CharacterPanel characterPanel, TerrainPanel terrainPanel,
      ItemPanel itemPanel) {
    this.actionGroup = actionGroup;
    this.characterPanel = characterPanel;
    this.terrainPanel = terrainPanel;
    this.itemPanel = itemPanel;
  }

  @Subscribe
  void selectCell(SelectCell selectCell) {
    Cell cell = selectCell.getObject();
    if (cell.hasCharacter()) {
      characterPanel.display(cell.getCharacter());
      terrainPanel.display(cell.getTerrain());
    } else {
      characterPanel.clear();
      terrainPanel.display(cell.getTerrain());
    }
  }

  @Subscribe
  void state(State state) {
    characterPanel.refresh();
    itemPanel.refresh();
    actionGroup.loadActions(state.getActions());
  }

  @Subscribe
  void playerState(PlayerState playerState) {
    characterPanel.display(playerState.getPlayer());
    // TODO: somehow show terrain? Use player action state?
  }

  @Subscribe
  void usingConsumable(UsingConsumable usingConsumable) {
    itemPanel.display(usingConsumable.getConsumable());
  }

  @Subscribe
  void selectingTarget(SelectingTarget selectingTarget) {
    itemPanel.display(selectingTarget.getWeapon());
  }

  @Subscribe
  void battling(Battling battling) {
    itemPanel.display(battling.getWeapon());
  }

  @Subscribe
  void exitState(ExitState exitState) {
    itemPanel.clear();
    actionGroup.clear();
  }

  @Subscribe
  void worldReset(WorldReset worldReset) {
    characterPanel.clear();
    itemPanel.clear();
    actionGroup.clear();
    terrainPanel.clear();
  }
}
