package com.jingyuyao.tactical.menu;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.jingyuyao.tactical.GameState;
import com.jingyuyao.tactical.data.TextLoader;
import com.jingyuyao.tactical.menu.MenuModule.MenuStage;
import com.kotcrab.vis.ui.building.StandardTableBuilder;
import com.kotcrab.vis.ui.building.TableBuilder;
import com.kotcrab.vis.ui.building.utilities.Alignment;
import com.kotcrab.vis.ui.building.utilities.CellWidget;
import com.kotcrab.vis.ui.building.utilities.Padding;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LevelResultMenu extends AbstractMenu {

  private final GameState gameState;
  private final TextLoader textLoader;
  private final VisLabel infoLabel;

  @Inject
  LevelResultMenu(
      GL20 gl,
      Input input,
      @MenuStage Stage stage,
      GameState gameState,
      TextLoader textLoader) {
    super(gl, input, stage);
    this.gameState = gameState;
    this.textLoader = textLoader;
    this.infoLabel = new VisLabel(null, Align.center);

    TableBuilder builder = new StandardTableBuilder(new Padding(20));
    builder.append(CellWidget.of(infoLabel).expandY().wrap());
    builder.row();

    builder
        .append(CellWidget.of(this.new ContinueButton()).align(Alignment.RIGHT).expandX().wrap());

    builder.build(getRoot());
  }

  public void setLevelResult(LevelResult levelResult) {
    switch (levelResult) {
      case WON:
        infoLabel.setText(textLoader.get(MenuBundle.LEVEL_WON));
        break;
      case LOST:
        infoLabel.setText(textLoader.get(MenuBundle.LEVEL_LOST));
        break;
    }
  }

  public enum LevelResult {
    WON, LOST,
  }

  private class ContinueButton extends VisTextButton {

    ContinueButton() {
      super(textLoader.get(MenuBundle.CONTINUE_BTN), "blue", new ChangeListener() {
        @Override
        public void changed(ChangeEvent event, Actor actor) {
          gameState.goToStartMenu();
        }
      });
      setName("continueButton");
      getLabelCell().pad(30);
    }
  }
}
