package com.jingyuyao.tactical.data;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class DataConfig {

  @Inject
  DataConfig() {

  }

  String getStartSaveFileName() {
    return "main.json";
  }

  String getMainSaveFileName() {
    return "main.save.json";
  }

  String getCharactersFileName(String mapName) {
    return String.format("maps/%s.json", mapName);
  }

  String getCharactersSaveFileName(String mapName) {
    return String.format("maps/%s.save.json", mapName);
  }

  String getTerrainsFileName(String mapName) {
    return String.format("maps/%s.tmx", mapName);
  }

  String getTerrainLayerKey() {
    return "terrain";
  }

  String getTerrainTypeKey() {
    return "type";
  }
}
