package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.building.StandardTableBuilder;
import com.kotcrab.vis.ui.building.TableBuilder;
import com.kotcrab.vis.ui.building.utilities.Alignment;
import com.kotcrab.vis.ui.building.utilities.CellWidget;
import com.kotcrab.vis.ui.building.utilities.Padding;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class WorldUILayout {

  private final ActionGroup actionGroup;
  private final SelectCellPanel selectCellPanel;
  private final ItemPanel itemPanel;
  private final TargetPanel targetPanel;

  @Inject
  WorldUILayout(
      ActionGroup actionGroup,
      SelectCellPanel selectCellPanel,
      ItemPanel itemPanel,
      TargetPanel targetPanel) {
    this.actionGroup = actionGroup;
    this.selectCellPanel = selectCellPanel;
    this.itemPanel = itemPanel;
    this.targetPanel = targetPanel;
  }

  Table rootTable() {
    TableBuilder builder = new StandardTableBuilder(Padding.PAD_8);
    builder.append(CellWidget.of(itemPanel).align(Alignment.TOP_LEFT).wrap());
    builder.append(CellWidget.of(selectCellPanel).align(Alignment.TOP_RIGHT).expandX().wrap());
    builder.row();

    builder.append(CellWidget.of(targetPanel).align(Alignment.TOP_LEFT).wrap());
    builder.append(
        CellWidget.of(actionGroup).align(Alignment.BOTTOM_RIGHT).expandY().expandX().wrap());

    Table table = builder.build();
    table.setFillParent(true);
    return table;
  }
}
