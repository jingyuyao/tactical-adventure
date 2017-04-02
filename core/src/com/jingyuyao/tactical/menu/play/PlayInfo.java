package com.jingyuyao.tactical.menu.play;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.google.common.base.Optional;
import com.jingyuyao.tactical.data.GameSave;
import com.jingyuyao.tactical.data.GameSaveManager;
import com.jingyuyao.tactical.data.LevelProgress;
import com.jingyuyao.tactical.data.LevelProgressManager;
import java.util.Locale;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class PlayInfo extends Label {

  private final GameSaveManager gameSaveManager;
  private final LevelProgressManager levelProgressManager;

  @Inject
  PlayInfo(Skin skin, GameSaveManager gameSaveManager, LevelProgressManager levelProgressManager) {
    super(null, skin);
    this.gameSaveManager = gameSaveManager;
    this.levelProgressManager = levelProgressManager;
    setAlignment(Align.center);
    updateText();
  }

  void updateText() {
    GameSave gameSave = gameSaveManager.load();
    int level = gameSave.getCurrentLevel();

    String progress = "No progress";
    Optional<LevelProgress> levelProgressOptional = levelProgressManager.load();
    if (levelProgressOptional.isPresent()) {
      LevelProgress levelProgress = levelProgressOptional.get();
      int activePlayers = levelProgress.getActivePlayers().size();
      int activeEnemies = levelProgress.getActiveEnemies().size();
      progress =
          String.format(
              Locale.US,
              "%d player characters, %d enemies remaining", activePlayers, activeEnemies);
    }

    setText(String.format(Locale.US, "Current level: %d\n%s", level, progress));
  }
}
