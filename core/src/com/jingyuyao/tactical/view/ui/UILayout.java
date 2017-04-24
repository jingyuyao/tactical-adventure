package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.event.SelectCell;
import com.jingyuyao.tactical.model.event.WorldReset;
import com.jingyuyao.tactical.model.state.Battling;
import com.jingyuyao.tactical.model.state.PlayerState;
import com.jingyuyao.tactical.model.state.SelectingTarget;
import com.jingyuyao.tactical.model.state.State;
import com.jingyuyao.tactical.model.state.UsingConsumable;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Coordinate;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class UILayout extends Table {

  private final ActionGroup actionGroup;
  private final CharacterPanel characterPanel;
  private final TerrainPanel terrainPanel;
  private final ItemPanel itemPanel;

  @Inject
  UILayout(
      ActionGroup actionGroup,
      CharacterPanel characterPanel,
      TerrainPanel terrainPanel,
      ItemPanel itemPanel) {
    this.actionGroup = actionGroup;
    this.characterPanel = characterPanel;
    this.terrainPanel = terrainPanel;
    this.itemPanel = itemPanel;

    setDebug(true);
    setFillParent(true);
    pad(10);

    Table left = new Table().debug();
    left.defaults().top().left();
    left.add(itemPanel).expand();

    Table mid = new Table().debug();

    Table right = new Table().debug();
    right.defaults().top().right();
    right.add(characterPanel);
    right.row();
    right.add(terrainPanel);
    right.row();
    right.add(actionGroup).bottom().expand();

    // fill() enables the sub-tables to distribute its own vertical space.
    // grow() causes the middle column to take up all the horizontal space.
    add(left).fill();
    add(mid).grow();
    add(right).fill();
  }

  void register(ModelBus modelBus) {
    modelBus.register(this);
  }

  @Subscribe
  void selectCell(SelectCell selectCell) {
    Cell cell = selectCell.getObject();
    Coordinate coordinate = cell.getCoordinate();
    Terrain terrain = cell.getTerrain();
    if (cell.hasCharacter()) {
      characterPanel.display(cell.getCharacter());
      terrainPanel.display(coordinate, terrain);
    } else {
      characterPanel.clear();
      terrainPanel.display(coordinate, terrain);
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
