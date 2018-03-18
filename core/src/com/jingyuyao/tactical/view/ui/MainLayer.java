package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.GameState;
import com.jingyuyao.tactical.data.TextLoader;
import com.jingyuyao.tactical.model.ModelBusListener;
import com.jingyuyao.tactical.model.event.ShowDialogues;
import com.kotcrab.vis.ui.building.OneColumnTableBuilder;
import com.kotcrab.vis.ui.building.OneRowTableBuilder;
import com.kotcrab.vis.ui.building.utilities.Alignment;
import com.kotcrab.vis.ui.building.utilities.CellWidget;
import com.kotcrab.vis.ui.building.utilities.Padding;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@ModelBusListener
class MainLayer extends VisTable {

  private static final int PAD = 20;
  private final GameState gameState;
  private final TextLoader textLoader;
  private final DialogueLayer dialogueLayer;

  @Inject
  MainLayer(
      ActionGroup actionGroup,
      SelectCellGroup selectCellGroup,
      ItemPanel itemPanel,
      TargetPanel targetPanel,
      TurnPanel turnPanel,
      GameState gameState,
      TextLoader textLoader,
      DialogueLayer dialogueLayer) {
    super(true);
    this.gameState = gameState;
    this.textLoader = textLoader;
    this.dialogueLayer = dialogueLayer;
    setFillParent(true);

    OneColumnTableBuilder leftColumn = new OneColumnTableBuilder();
    leftColumn.append(
        CellWidget.of(itemPanel)
            .align(Alignment.LEFT)
            .padding(Padding.of(PAD, PAD, PAD, 0))
            .wrap());
    leftColumn.append(
        CellWidget.of(targetPanel)
            .align(Alignment.LEFT)
            .padding(Padding.of(0, PAD, PAD, 0))
            .wrap());
    leftColumn.append(
        CellWidget.of(turnPanel)
            .align(Alignment.BOTTOM_LEFT)
            .padding(Padding.of(0, PAD, PAD, 0))
            .expandY()
            .wrap());
    leftColumn.append(
        CellWidget.of(this.new MenuButton())
            .align(Alignment.LEFT)
            .padding(Padding.of(0, PAD, PAD, 0))
            .wrap());

    OneColumnTableBuilder rightColumn = new OneColumnTableBuilder();
    rightColumn.append(
        CellWidget.of(selectCellGroup)
            .align(Alignment.RIGHT)
            .padding(Padding.of(PAD, 0, PAD, PAD))
            .wrap());
    rightColumn.append(
        CellWidget.of(actionGroup)
            .align(Alignment.BOTTOM_RIGHT)
            .padding(Padding.of(0, 0, PAD, PAD))
            .expandY()
            .expandX()
            .wrap());

    OneRowTableBuilder mainBuilder = new OneRowTableBuilder();
    mainBuilder.append(
        CellWidget.of(leftColumn.build())
            .expandY()
            .fillY()
            .wrap());
    mainBuilder.append(
        CellWidget.of(rightColumn.build())
            .expandX()
            .expandY()
            .fillX()
            .fillY()
            .wrap());
    mainBuilder.build(this);
  }

  @Subscribe
  void showDialogues(ShowDialogues showDialogues) {
    dialogueLayer.display(showDialogues);
  }

  class MenuButton extends VisTextButton {

    MenuButton() {
      super(textLoader.get(UIBundle.MENU_BTN), "blue", new ChangeListener() {
        @Override
        public void changed(ChangeEvent event, Actor actor) {
          gameState.goToStartMenu();
        }
      });
      setName("menuButton");
      getLabelCell().pad(15);
    }
  }
}
