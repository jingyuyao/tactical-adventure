package com.jingyuyao.tactical.data;

import com.jingyuyao.tactical.model.resource.ModelBundle;
import com.jingyuyao.tactical.model.resource.ResourceKeyBundle;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class DataConfig {

  @Inject
  DataConfig() {

  }

  String getInitFileName() {
    return "data/init.json";
  }

  String getScriptFileName() {
    return "data/script.json";
  }

  String getMainSaveFileName() {
    return "save/main.save.json";
  }

  String getMainLevelProgressFileName() {
    return "save/main.progress.save.json";
  }

  String getLevelDir(int level) {
    return "data/" + level + "/";
  }

  String getLevelInitFileName(int level) {
    return getLevelDir(level) + "init.json";
  }

  String getLevelScriptFileName(int level) {
    return getLevelDir(level) + "script.json";
  }

  String getLevelTerrainFileName(int level) {
    return getLevelDir(level) + "terrains.tmx";
  }

  String getTerrainLayerKey() {
    return "terrain";
  }

  String getTerrainTypeKey() {
    return "type";
  }

  ResourceKeyBundle getLevelDialogueBundle(int level) {
    return ModelBundle.getLevelDialogue(level);
  }

  ResourceKeyBundle getDeathDialogueBundle() {
    return ModelBundle.DEATH_DIALOGUE;
  }

  ResourceKeyBundle getPersonNameBundle() {
    return ModelBundle.PERSON_NAME;
  }
}
