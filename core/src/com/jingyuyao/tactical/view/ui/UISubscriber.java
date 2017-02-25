package com.jingyuyao.tactical.view.ui;

import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.event.SelectCharacter;
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
  void selectCharacter(SelectCharacter<Character> selectCharacter) {
    characterPanel.display(selectCharacter.getObject());
    terrainPanel.display(terrains.get(selectCharacter.getObject().getCoordinate()));
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
