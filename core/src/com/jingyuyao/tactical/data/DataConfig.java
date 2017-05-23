package com.jingyuyao.tactical.data;

import com.jingyuyao.tactical.model.resource.ModelBundle;
import com.jingyuyao.tactical.model.resource.ResourceKeyBundle;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DataConfig {

  @Inject
  DataConfig() {

  }

  String getInitFileName() {
    return "data/init.json";
  }

  String getMainSaveFileName() {
    return "save/main.save.json";
  }

  String getMainLevelSaveFileName() {
    return "save/main.level.save.json";
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

  public String getLevelTerrainFileName(int level) {
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
