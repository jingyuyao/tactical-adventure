package com.jingyuyao.tactical.menu.play;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.google.common.base.Optional;
import com.jingyuyao.tactical.data.DataManager;
import com.jingyuyao.tactical.data.GameSave;
import com.jingyuyao.tactical.data.LevelProgress;
import java.util.Locale;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class PlayInfo extends Label {

  private final DataManager dataManager;

  @Inject
  PlayInfo(Skin skin, DataManager dataManager) {
    super(null, skin);
    this.dataManager = dataManager;
    setAlignment(Align.center);
    updateText();
  }

  void updateText() {
    GameSave gameSave = dataManager.loadCurrentSave();
    int level = gameSave.getCurrentLevel();

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

    setText(String.format(Locale.US, "Current level: %d\n%s", level, progress));
  }
}
