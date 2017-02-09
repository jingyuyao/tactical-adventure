package com.jingyuyao.tactical.view;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.event.SelectEnemy;
import com.jingyuyao.tactical.model.event.SelectPlayer;
import com.jingyuyao.tactical.model.event.SelectTerrain;
import com.jingyuyao.tactical.model.map.Terrains;
import com.jingyuyao.tactical.model.state.Battling;
import com.jingyuyao.tactical.model.state.SelectingTarget;
import com.jingyuyao.tactical.model.state.State;
import com.jingyuyao.tactical.view.ViewModule.MapUIStage;
import com.jingyuyao.tactical.view.ui.ActionGroup;
import com.jingyuyao.tactical.view.ui.CharacterInfo;
import com.jingyuyao.tactical.view.ui.TerrainInfo;
import com.jingyuyao.tactical.view.ui.WeaponInfo;
import javax.inject.Inject;
import javax.inject.Singleton;

// TODO: need to refresh stats after attack
@Singleton
class MapUI {

  private final Stage stage;
  private final ActionGroup actionGroup;
  private final CharacterInfo characterInfo;
  private final TerrainInfo terrainInfo;
  private final WeaponInfo weaponInfo;
  private final Terrains terrains;

  @Inject
  MapUI(
      @MapUIStage Stage stage,
      ActionGroup actionGroup,
      CharacterInfo characterInfo,
      TerrainInfo terrainInfo,
      WeaponInfo weaponInfo,
      Terrains terrains) {
    this.stage = stage;
    this.terrainInfo = terrainInfo;
    this.weaponInfo = weaponInfo;
    this.terrains = terrains;
    this.actionGroup = actionGroup;
    this.characterInfo = characterInfo;
  }

  @Subscribe
  public void selectPlayer(SelectPlayer selectPlayer) {
    characterInfo.display(selectPlayer.getObject());
    terrainInfo.display(terrains.get(selectPlayer.getObject().getCoordinate()));
  }

  @Subscribe
  public void selectEnemy(SelectEnemy selectEnemy) {
    characterInfo.display(selectEnemy.getObject());
    terrainInfo.display(terrains.get(selectEnemy.getObject().getCoordinate()));
  }

  @Subscribe
  public void selectTerrain(SelectTerrain selectTerrain) {
    characterInfo.clear();
    terrainInfo.display(selectTerrain.getObject());
  }

  @Subscribe
  public void state(State state) {
    actionGroup.loadActions(state.getActions());
  }

  @Subscribe
  public void selectingTarget(SelectingTarget selectingTarget) {
    weaponInfo.display(selectingTarget.getWeapon());
  }

  @Subscribe
  public void battling(Battling battling) {
    weaponInfo.display(battling.getWeapon());
  }

  @Subscribe
  public void exitState(ExitState exitState) {
    weaponInfo.clear();
    actionGroup.clear();
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
}
