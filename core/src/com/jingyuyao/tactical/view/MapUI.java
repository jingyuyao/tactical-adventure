package com.jingyuyao.tactical.view;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.event.SelectEnemy;
import com.jingyuyao.tactical.model.event.SelectPlayer;
import com.jingyuyao.tactical.model.event.SelectTerrain;
import com.jingyuyao.tactical.model.map.Terrains;
import com.jingyuyao.tactical.model.state.PlayerState;
import com.jingyuyao.tactical.model.state.State;
import com.jingyuyao.tactical.view.ViewModule.MapUIStage;
import com.jingyuyao.tactical.view.ui.ActionGroup;
import com.jingyuyao.tactical.view.ui.PrimaryInfo;
import com.jingyuyao.tactical.view.ui.SecondaryInfo;
import javax.inject.Inject;
import javax.inject.Singleton;

// TODO: need to refresh stats after attack
@Singleton
class MapUI {

  private final Terrains terrains;
  private final Stage stage;
  private final ActionGroup actionGroup;
  private final SecondaryInfo secondaryInfo;
  private final PrimaryInfo primaryInfo;

  @Inject
  MapUI(
      Terrains terrains,
      @MapUIStage Stage stage,
      ActionGroup actionGroup,
      SecondaryInfo secondaryInfo,
      PrimaryInfo primaryInfo) {
    this.terrains = terrains;
    this.stage = stage;
    this.actionGroup = actionGroup;
    this.secondaryInfo = secondaryInfo;
    this.primaryInfo = primaryInfo;
  }

  @Subscribe
  public void selectPlayer(SelectPlayer selectPlayer) {
    displayPlayer(selectPlayer.getObject());
  }

  @Subscribe
  public void selectEnemy(SelectEnemy selectEnemy) {
    Enemy enemy = selectEnemy.getObject();
    secondaryInfo.display(enemy, terrains.get(enemy.getCoordinate()));
  }

  @Subscribe
  public void selectTerrain(SelectTerrain selectTerrain) {
    secondaryInfo.display(selectTerrain.getObject());
  }

  @Subscribe
  public void state(State state) {
    actionGroup.reloadActions(state.getActions());
  }

  @Subscribe
  public void playerState(PlayerState playerState) {
    displayPlayer(playerState.getPlayer());
  }

  void act(float delta) {
    stage.act(delta);
  }

  void draw() {
    stage.getViewport().apply();
    stage.draw();
  }

  void resize(int width, int height) {
    stage.getViewport().update(width, height);
  }

  void dispose() {
    stage.dispose();
  }

  private void displayPlayer(Player player) {
    primaryInfo.display(player, terrains.get(player.getCoordinate()));
    secondaryInfo.clear();
  }
}
