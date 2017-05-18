package com.jingyuyao.tactical.data;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import com.google.common.base.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class GameDataManager {

  private final DataConfig dataConfig;
  private final MyGson myGson;
  private final Files files;

  @Inject
  GameDataManager(DataConfig dataConfig, MyGson myGson, Files files) {
    this.dataConfig = dataConfig;
    this.myGson = myGson;
    this.files = files;
  }

  GameData loadData() {
    Optional<GameData> main = loadData(dataConfig.getMainSaveFileName(), false);
    if (main.isPresent()) {
      return main.get();
    }

    Optional<GameData> start = loadData(dataConfig.getInitFileName(), true);
    if (start.isPresent()) {
      GameData startSave = start.get();
      saveData(startSave);
      return startSave;
    }

    throw new IllegalStateException("Could not find main init or save file!");
  }

  GameScript loadScript() {
    FileHandle fileHandle = files.internal(dataConfig.getScriptFileName());
    return myGson.fromJson(fileHandle.readString(), GameScript.class);
  }

  void saveData(GameData gameData) {
    FileHandle fileHandle = files.local(dataConfig.getMainSaveFileName());
    fileHandle.writeString(myGson.toJson(gameData), false);
  }

  void removeSavedData() {
    FileHandle fileHandle = files.local(dataConfig.getMainSaveFileName());
    if (fileHandle.exists()) {
      fileHandle.delete();
    }
  }

  private Optional<GameData> loadData(String fileName, boolean internal) {
    FileHandle fileHandle = internal ? files.internal(fileName) : files.local(fileName);
    if (fileHandle.exists()) {
      return Optional.of(myGson.fromJson(fileHandle.readString(), GameData.class));
    }
    return Optional.absent();
  }
}
