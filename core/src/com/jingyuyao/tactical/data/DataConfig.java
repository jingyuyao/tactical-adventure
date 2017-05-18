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

  String getStartSaveFileName() {
    return "start.json";
  }

  String getMainSaveFileName() {
    return "main.save.json";
  }

  String getMainLevelProgressFileName() {
    return "main.progress.save.json";
  }

  String getLevelShipsFileName(int level) {
    return "levels/" + level + "/ships.json";
  }

  String getLevelTerrainFileName(int level) {
    return "levels/" + level + "/terrains.tmx";
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
