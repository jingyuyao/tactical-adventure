package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
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
import com.jingyuyao.tactical.view.ui.UIModule.WorldUIStage;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WorldUI {

  private final Stage stage;
  private final ActionGroup actionGroup;
  private final CharacterPanel characterPanel;
  private final TerrainPanel terrainPanel;
  private final ItemPanel itemPanel;
  private final Terrains terrains;

  @Inject
  WorldUI(
      @WorldUIStage Stage stage,
      ActionGroup actionGroup,
      CharacterPanel characterPanel,
      TerrainPanel terrainPanel,
      ItemPanel itemPanel,
      Terrains terrains) {
    this.stage = stage;
    this.terrainPanel = terrainPanel;
    this.itemPanel = itemPanel;
    this.terrains = terrains;
    this.actionGroup = actionGroup;
    this.characterPanel = characterPanel;
  }

  @Subscribe
  public void selectPlayer(SelectPlayer selectPlayer) {
    characterPanel.display(selectPlayer.getObject());
    terrainPanel.display(terrains.get(selectPlayer.getObject().getCoordinate()));
  }

  @Subscribe
  public void selectEnemy(SelectEnemy selectEnemy) {
    characterPanel.display(selectEnemy.getObject());
    terrainPanel.display(terrains.get(selectEnemy.getObject().getCoordinate()));
  }

  @Subscribe
  public void selectTerrain(SelectTerrain selectTerrain) {
    characterPanel.clear();
    terrainPanel.display(selectTerrain.getObject());
  }

  @Subscribe
  public void state(State state) {
    characterPanel.refresh();
    itemPanel.refresh();
    actionGroup.loadActions(state.getActions());
  }

  @Subscribe
  public void playerState(PlayerState playerState) {
    characterPanel.display(playerState.getPlayer());
    terrainPanel.display(terrains.get(playerState.getPlayer().getCoordinate()));
  }

  @Subscribe
  public void usingConsumable(UsingConsumable usingConsumable) {
    itemPanel.display(usingConsumable.getConsumable());
  }

  @Subscribe
  public void selectingTarget(SelectingTarget selectingTarget) {
    itemPanel.display(selectingTarget.getWeapon());
  }

  @Subscribe
  public void battling(Battling battling) {
    itemPanel.display(battling.getWeapon());
  }

  @Subscribe
  public void exitState(ExitState exitState) {
    itemPanel.clear();
    actionGroup.clear();
  }

  public void act(float delta) {
    stage.act(delta);
  }

  public void draw() {
    stage.getViewport().apply();
    stage.draw();
  }

  public void resize(int width, int height) {
    stage.getViewport().update(width, height);
  }

  public void dispose() {
    stage.dispose();
  }
}
