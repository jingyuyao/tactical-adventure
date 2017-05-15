package com.jingyuyao.tactical.view.ui;

import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.ModelBusListener;
import com.jingyuyao.tactical.model.event.ShowDialogues;
import com.kotcrab.vis.ui.building.StandardTableBuilder;
import com.kotcrab.vis.ui.building.TableBuilder;
import com.kotcrab.vis.ui.building.utilities.Alignment;
import com.kotcrab.vis.ui.building.utilities.CellWidget;
import com.kotcrab.vis.ui.building.utilities.Padding;
import com.kotcrab.vis.ui.widget.VisTable;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@ModelBusListener
class MainLayer extends VisTable {

  private final DialogueLayer dialogueLayer;

  @Inject
  MainLayer(
      ActionGroup actionGroup,
      SelectCellGroup selectCellGroup,
      ItemPanel itemPanel,
      TargetPanel targetPanel,
      DialogueLayer dialogueLayer) {
    super(true);
    this.dialogueLayer = dialogueLayer;
    setFillParent(true);

    TableBuilder builder = new StandardTableBuilder(new Padding(20));
    builder.append(CellWidget.of(itemPanel).align(Alignment.TOP_LEFT).wrap());
    builder.append(CellWidget.of(selectCellGroup).align(Alignment.TOP_RIGHT).expandX().wrap());
    builder.row();

    builder.append(CellWidget.of(targetPanel).align(Alignment.TOP_LEFT).wrap());
    builder.append(
        CellWidget.of(actionGroup).align(Alignment.BOTTOM_RIGHT).expandY().expandX().wrap());

    builder.build(this);
  }

  @Subscribe
  void showDialogues(ShowDialogues showDialogues) {
    dialogueLayer.display(showDialogues);
  }
}
