package com.jingyuyao.tactical.data;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class DataConfig {

  @Inject
  DataConfig() {

  }

  String getStartSaveFileName() {
    return "start.json";
  }

  String getMainSaveFileName() {
    return "main.save.json";
  }

  String getMainLevelProgressFileName() {
    return "main.progress.save.json";
  }

  String getLevelDataFileName(int level) {
    return "levels/" + level + ".level.json";
  }

  String getLevelMapFileName(int level) {
    return "levels/" + level + ".level.tmx";
  }

  String getDefaultLevelDialogueFileName(int level) {
    return getLevelDialogueBundle(level) + ".properties";
  }

  String getLevelDialogueBundle(int level) {
    return "i18n/model/script/dialogue/Level" + level;
  }

  String getTerrainLayerKey() {
    return "terrain";
  }

  String getTerrainTypeKey() {
    return "type";
  }
}
