package com.jingyuyao.tactical.view.ui;

import com.kotcrab.vis.ui.building.StandardTableBuilder;
import com.kotcrab.vis.ui.building.TableBuilder;
import com.kotcrab.vis.ui.building.utilities.Alignment;
import com.kotcrab.vis.ui.building.utilities.CellWidget;
import com.kotcrab.vis.ui.building.utilities.Padding;
import com.kotcrab.vis.ui.widget.VisTable;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class WorldUILayout extends VisTable {

  @Inject
  WorldUILayout(
      ActionGroup actionGroup,
      SelectCellPanel selectCellPanel,
      ItemPanel itemPanel,
      TargetPanel targetPanel) {
    super(true);
    setFillParent(true);

    TableBuilder builder = new StandardTableBuilder(Padding.PAD_8);
    builder.append(CellWidget.of(itemPanel).align(Alignment.TOP_LEFT).wrap());
    builder.append(CellWidget.of(selectCellPanel).align(Alignment.TOP_RIGHT).expandX().wrap());
    builder.row();

    builder.append(CellWidget.of(targetPanel).align(Alignment.TOP_LEFT).wrap());
    builder.append(
        CellWidget.of(actionGroup).align(Alignment.BOTTOM_RIGHT).expandY().expandX().wrap());

    builder.build(this);
  }
}
