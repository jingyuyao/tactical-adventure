package com.jingyuyao.tactical.menu;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.google.common.base.Optional;
import com.jingyuyao.tactical.GameState;
import com.jingyuyao.tactical.data.DataManager;
import com.jingyuyao.tactical.data.GameSave;
import com.jingyuyao.tactical.data.LevelSave;
import com.jingyuyao.tactical.data.TextLoader;
import com.jingyuyao.tactical.menu.MenuModule.MenuStage;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.ship.ShipGroup;
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
public class StartMenu extends AbstractMenu {

  private final GameState gameState;
  private final DataManager dataManager;
  private final VisLabel infoLabel;
  private final TextLoader textLoader;

  @Inject
  StartMenu(
      GL20 gl,
      Input input,
      @MenuStage Stage stage,
      GameState gameState,
      DataManager dataManager,
      TextLoader textLoader) {
    super(gl, input, stage);
    this.gameState = gameState;
    this.dataManager = dataManager;
    this.textLoader = textLoader;
    this.infoLabel = new VisLabel(null, Align.center);
    this.infoLabel.setName("infoLabel");

    TableBuilder builder = new StandardTableBuilder(new Padding(20));
    builder.append(CellWidget.of(infoLabel).expandY().wrap());
    builder.row();

    builder.append(CellWidget.of(this.new ResetButton()).align(Alignment.LEFT).expandX().wrap());
    builder.append(this.new PlayButton());

    builder.build(getRoot());
  }

  @Override
  public void show() {
    super.show();
    infoLabel.setText(getInfo());
  }

  private String getInfo() {
    String progress;
    Optional<LevelSave> levelProgressOptional = dataManager.loadLevelSave();
    if (levelProgressOptional.isPresent()) {
      LevelSave levelSave = levelProgressOptional.get();
      int activePlayers = 0;
      int activeEnemies = 0;
      for (Ship ship : levelSave.getShips().values()) {
        if (ship.inGroup(ShipGroup.PLAYER)) {
          activePlayers++;
        }
        if (ship.inGroup(ShipGroup.ENEMY)) {
          activeEnemies++;
        }
      }
      progress = textLoader.get(MenuBundle.HAS_PROGRESS.format(activePlayers, activeEnemies));
    } else {
      progress = textLoader.get(MenuBundle.NO_PROGRESS);
    }

    GameSave gameSave = dataManager.loadGameSave();
    int level = gameSave.getCurrentLevel();
    return textLoader.get(MenuBundle.LEVEL_INFO.format(level, progress));
  }

  class PlayButton extends VisTextButton {

    PlayButton() {
      super(textLoader.get(MenuBundle.PLAY_BTN), "blue", new ChangeListener() {
        @Override
        public void changed(ChangeEvent event, Actor actor) {
          gameState.play();
        }
      });
      setName("playButton");
      getLabelCell().pad(30);
    }
  }

  class ResetButton extends VisTextButton {

    ResetButton() {
      super(textLoader.get(MenuBundle.RESET_BTN), new ChangeListener() {
        @Override
        public void changed(ChangeEvent event, Actor actor) {
          gameState.reset();
          infoLabel.setText(getInfo());
        }
      });
      setName("resetButton");
      getLabelCell().pad(30);
    }
  }
}
