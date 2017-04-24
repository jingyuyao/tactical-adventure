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
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class UILayout extends Table {

  private final ActionGroup actionGroup;
  private final SelectCellPanel selectCellPanel;
  private final ItemPanel itemPanel;

  @Inject
  UILayout(ActionGroup actionGroup, SelectCellPanel selectCellPanel, ItemPanel itemPanel) {
    this.actionGroup = actionGroup;
    this.selectCellPanel = selectCellPanel;
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
    right.add(selectCellPanel);
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
    actionGroup.clear();
    itemPanel.reset();
  }
}
