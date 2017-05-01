package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.ModelBusListener;
import com.jingyuyao.tactical.model.event.SelectCell;
import com.jingyuyao.tactical.model.event.WorldReset;
import com.jingyuyao.tactical.model.state.State;
import com.jingyuyao.tactical.model.world.Cell;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@ModelBusListener
class SelectCellGroup extends VerticalGroup {

  private final CharacterOverviewPanel characterOverviewPanel;
  private final TerrainOverviewPanel terrainOverviewPanel;
  private final CoordinatePanel coordinatePanel;

  @Inject
  SelectCellGroup(
      CharacterOverviewPanel characterOverviewPanel,
      TerrainOverviewPanel terrainOverviewPanel,
      CoordinatePanel coordinatePanel) {
    this.characterOverviewPanel = characterOverviewPanel;
    this.terrainOverviewPanel = terrainOverviewPanel;
    this.coordinatePanel = coordinatePanel;
    space(10);
    columnRight();
    addActor(characterOverviewPanel);
    addActor(terrainOverviewPanel);
    addActor(coordinatePanel);
  }

  @Subscribe
  void selectCell(SelectCell selectCell) {
    Cell cell = selectCell.getObject();
    if (cell.hasCharacter()) {
      characterOverviewPanel.display(cell.getCharacter());
    } else {
      characterOverviewPanel.clearDisplay();
    }
    terrainOverviewPanel.display(cell.getTerrain());
    coordinatePanel.display(cell.getCoordinate());
  }

  @Subscribe
  void state(State state) {
    characterOverviewPanel.refresh();
    terrainOverviewPanel.refresh();
    coordinatePanel.refresh();
  }

  @Subscribe
  void worldReset(WorldReset worldReset) {
    characterOverviewPanel.clearDisplay();
    terrainOverviewPanel.clearDisplay();
    coordinatePanel.clearDisplay();
  }
}
