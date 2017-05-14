package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.Gdx;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.ModelBusListener;
import com.jingyuyao.tactical.model.event.ShowDialogues;
import com.jingyuyao.tactical.model.script.Dialogue;
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

  @Inject
  MainLayer(
      ActionGroup actionGroup,
      SelectCellGroup selectCellGroup,
      ItemPanel itemPanel,
      TargetPanel targetPanel) {
    super(true);
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
    // TODO: temp
    String text = Joiner.on("\n")
        .join(FluentIterable.from(showDialogues.getDialogues()).transform(
            new Function<Dialogue, String>() {
              @Override
              public String apply(Dialogue input) {
                return input.getMessage().getKey();
              }
            }));
    Gdx.app.log("dialogue", text);
    showDialogues.complete();
  }
}
