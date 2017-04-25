package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.ModelBusListener;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.event.SelectCell;
import com.jingyuyao.tactical.model.event.WorldReset;
import com.jingyuyao.tactical.model.state.Battling;
import com.jingyuyao.tactical.model.state.PlayerState;
import com.jingyuyao.tactical.model.state.SelectingTarget;
import com.jingyuyao.tactical.model.state.State;
import com.jingyuyao.tactical.model.state.UsingConsumable;
import com.kotcrab.vis.ui.building.StandardTableBuilder;
import com.kotcrab.vis.ui.building.TableBuilder;
import com.kotcrab.vis.ui.building.utilities.Alignment;
import com.kotcrab.vis.ui.building.utilities.CellWidget;
import com.kotcrab.vis.ui.building.utilities.Padding;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@ModelBusListener
class WorldUILayout {

  private final ActionGroup actionGroup;
  private final SelectCellPanel selectCellPanel;
  private final ItemPanel itemPanel;

  /**
   * All components are injected so this object can handle events immediately. This means none
   * of these objects can be concrete widgets.
   */
  @Inject
  WorldUILayout(ActionGroup actionGroup, SelectCellPanel selectCellPanel, ItemPanel itemPanel) {
    this.actionGroup = actionGroup;
    this.selectCellPanel = selectCellPanel;
    this.itemPanel = itemPanel;
  }

  Table rootTable() {
    TableBuilder builder = new StandardTableBuilder(Padding.PAD_8);
    builder.append(CellWidget.of(itemPanel).align(Alignment.TOP_LEFT).wrap());
    builder.append(CellWidget.of(selectCellPanel).align(Alignment.TOP_RIGHT).expandX().wrap());
    builder.row();

    builder.append(
        CellWidget.of(actionGroup).align(Alignment.BOTTOM_RIGHT).expandY().expandX().wrap());

    Table table = builder.build();
    table.setFillParent(true);
    return table;
  }

  @Subscribe
  void selectCell(SelectCell selectCell) {
    selectCellPanel.display(selectCell.getObject());
  }

  @Subscribe
  void state(State state) {
    selectCellPanel.refresh();
    itemPanel.refresh();
    actionGroup.loadActions(state.getActions());
  }

  @Subscribe
  void playerState(PlayerState playerState) {
    selectCellPanel.refresh();
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
    itemPanel.reset();
    actionGroup.clear();
  }

  @Subscribe
  void worldReset(WorldReset worldReset) {
    selectCellPanel.reset();
    itemPanel.reset();
    actionGroup.clear();
  }
}
