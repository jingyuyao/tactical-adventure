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

  private final ShipOverviewPanel shipOverviewPanel;
  private final TerrainOverviewPanel terrainOverviewPanel;
  private final CoordinatePanel coordinatePanel;
  private Cell cell;

  @Inject
  SelectCellGroup(
      ShipOverviewPanel shipOverviewPanel,
      TerrainOverviewPanel terrainOverviewPanel,
      CoordinatePanel coordinatePanel) {
    this.shipOverviewPanel = shipOverviewPanel;
    this.terrainOverviewPanel = terrainOverviewPanel;
    this.coordinatePanel = coordinatePanel;
    space(10);
    columnRight();
    addActor(shipOverviewPanel);
    addActor(terrainOverviewPanel);
    addActor(coordinatePanel);
  }

  @Subscribe
  void selectCell(SelectCell selectCell) {
    display(selectCell.getObject());
  }

  @Subscribe
  void state(State state) {
    if (cell != null) {
      display(cell);
    }
  }

  @Subscribe
  void worldReset(WorldReset worldReset) {
    cell = null;
    shipOverviewPanel.clearDisplay();
    terrainOverviewPanel.clearDisplay();
    coordinatePanel.clearDisplay();
  }

  private void display(Cell cell) {
    this.cell = cell;
    if (cell.ship().isPresent()) {
      shipOverviewPanel.display(cell.ship().get());
    } else {
      shipOverviewPanel.clearDisplay();
    }
    terrainOverviewPanel.display(cell.getTerrain());
    coordinatePanel.display(cell.getCoordinate());
  }
}
