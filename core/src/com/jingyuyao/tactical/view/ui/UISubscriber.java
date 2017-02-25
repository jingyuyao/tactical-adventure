package com.jingyuyao.tactical.view.ui;

import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.event.SelectEnemy;
import com.jingyuyao.tactical.model.event.SelectPlayer;
import com.jingyuyao.tactical.model.event.SelectTerrain;
import com.jingyuyao.tactical.model.map.Terrains;
import com.jingyuyao.tactical.model.state.Battling;
import com.jingyuyao.tactical.model.state.PlayerState;
import com.jingyuyao.tactical.model.state.SelectingTarget;
import com.jingyuyao.tactical.model.state.State;
import com.jingyuyao.tactical.model.state.UsingConsumable;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UISubscriber {

  private final ActionGroup actionGroup;
  private final CharacterPanel characterPanel;
  private final TerrainPanel terrainPanel;
  private final ItemPanel itemPanel;
  private final Terrains terrains;

  @Inject
  UISubscriber(ActionGroup actionGroup, CharacterPanel characterPanel,
      TerrainPanel terrainPanel, ItemPanel itemPanel, Terrains terrains) {
    this.actionGroup = actionGroup;
    this.characterPanel = characterPanel;
    this.terrainPanel = terrainPanel;
    this.itemPanel = itemPanel;
    this.terrains = terrains;
  }

  @Subscribe
  void selectPlayer(SelectPlayer selectPlayer) {
    characterPanel.display(selectPlayer.getObject());
    terrainPanel.display(terrains.get(selectPlayer.getObject().getCoordinate()));
  }

  @Subscribe
  void selectEnemy(SelectEnemy selectEnemy) {
    characterPanel.display(selectEnemy.getObject());
    terrainPanel.display(terrains.get(selectEnemy.getObject().getCoordinate()));
  }

  @Subscribe
  void selectTerrain(SelectTerrain selectTerrain) {
    characterPanel.clear();
    terrainPanel.display(selectTerrain.getObject());
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
    terrainPanel.display(terrains.get(playerState.getPlayer().getCoordinate()));
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
}
