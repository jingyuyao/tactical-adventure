package com.jingyuyao.tactical.data;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class DataConfig {

  @Inject
  DataConfig() {

  }

  String getCharactersFileName(String mapName) {
    return String.format("maps/%s.json", mapName);
  }

  String getTerrainsFileName(String mapName) {
    return String.format("maps/%s.tmx", mapName);
  }

  String getMapSaveFileName(String mapName) {
    return String.format("maps/%s.save.json", mapName);
  }

  String getTerrainLayerKey() {
    return "terrain";
  }

  String getTerrainTypeKey() {
    return "type";
  }
}
