package com.jingyuyao.tactical.screen;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.google.common.base.Optional;
import com.jingyuyao.tactical.GameState;
import com.jingyuyao.tactical.data.DataManager;
import com.jingyuyao.tactical.data.GameSave;
import com.jingyuyao.tactical.data.LevelProgress;
import com.kotcrab.vis.ui.building.StandardTableBuilder;
import com.kotcrab.vis.ui.building.TableBuilder;
import com.kotcrab.vis.ui.building.utilities.Alignment;
import com.kotcrab.vis.ui.building.utilities.CellWidget;
import com.kotcrab.vis.ui.building.utilities.Padding;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import java.util.Locale;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class StartScreenLayout {

  private final GameState gameState;
  private final DataManager dataManager;
  private VisLabel infoLabel;

  @Inject
  StartScreenLayout(GameState gameState, DataManager dataManager) {
    this.gameState = gameState;
    this.dataManager = dataManager;
  }

  Table rootTable() {
    infoLabel = new VisLabel(getInfoText(), Align.center);
    VisTextButton play = new VisTextButton("Play", new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        gameState.play();
      }
    });
    VisTextButton reset = new VisTextButton("Reset", new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        gameState.reset();
        infoLabel.setText(getInfoText());
      }
    });

    TableBuilder builder = new StandardTableBuilder(Padding.PAD_8);
    builder.append(CellWidget.of(infoLabel).expandY().wrap());
    builder.row();

    builder.append(CellWidget.of(play).align(Alignment.LEFT).expandX().wrap());
    builder.append(reset);

    Table table = builder.build().debug();
    table.setFillParent(true);
    return table;
  }

  private String getInfoText() {
    String progress = "No progress";
    Optional<LevelProgress> levelProgressOptional = dataManager.loadCurrentProgress();
    if (levelProgressOptional.isPresent()) {
      LevelProgress levelProgress = levelProgressOptional.get();
      int activePlayers = levelProgress.getActivePlayers().size();
      int activeEnemies = levelProgress.getActiveEnemies().size();
      progress =
          String.format(
              Locale.US,
              "%d player characters, %d enemies remaining", activePlayers, activeEnemies);
    }

    GameSave gameSave = dataManager.loadCurrentSave();
    int level = gameSave.getCurrentLevel();
    return String.format(Locale.US, "Current level: %d\n%s", level, progress);
  }
}
