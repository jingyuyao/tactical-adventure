package com.jingyuyao.tactical.data;

import com.jingyuyao.tactical.model.resource.KeyBundle;
import com.jingyuyao.tactical.model.resource.ModelBundle;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class DataConfig {

  @Inject
  DataConfig() {

  }

  String getInitFileName() {
    return "data/init.conf";
  }

  String getMainSaveFileName() {
    return "save/main.save.bin";
  }

  String getMainLevelSaveFileName() {
    return "save/main.level.save.bin";
  }

  String getLevelDir(int level) {
    return "data/" + level + "/";
  }

  String getLevelWorldFileName(int level) {
    return getLevelDir(level) + "world.json";
  }

  String getLevelScriptFileName(int level) {
    return getLevelDir(level) + "script.json";
  }

  String getLevelTerrainFileName(int level) {
    return getLevelDir(level) + "terrains.json";
  }

  KeyBundle getLevelDialogueBundle(int level) {
    return ModelBundle.getLevelDialogue(level);
  }

  KeyBundle getDeathDialogueBundle() {
    return ModelBundle.DEATH_DIALOGUE;
  }

  KeyBundle getPersonNameBundle() {
    return ModelBundle.PERSON_NAME;
  }
}
