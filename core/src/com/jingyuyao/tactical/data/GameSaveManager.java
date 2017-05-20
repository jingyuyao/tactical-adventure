package com.jingyuyao.tactical.data;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import com.google.common.base.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class GameSaveManager {

  private final DataConfig dataConfig;
  private final MyGson myGson;
  private final Files files;

  @Inject
  GameSaveManager(DataConfig dataConfig, MyGson myGson, Files files) {
    this.dataConfig = dataConfig;
    this.myGson = myGson;
    this.files = files;
  }

  GameSave load() {
    Optional<GameSave> main = load(dataConfig.getMainSaveFileName(), false);
    if (main.isPresent()) {
      return main.get();
    }

    Optional<GameSave> start = load(dataConfig.getInitFileName(), true);
    if (start.isPresent()) {
      GameSave startSave = start.get();
      save(startSave);
      return startSave;
    }

    throw new IllegalStateException("Could not find main init or save file!");
  }

  void save(GameSave gameSave) {
    FileHandle fileHandle = files.local(dataConfig.getMainSaveFileName());
    fileHandle.writeString(myGson.toJson(gameSave), false);
  }

  void removeSavedData() {
    FileHandle fileHandle = files.local(dataConfig.getMainSaveFileName());
    if (fileHandle.exists()) {
      fileHandle.delete();
    }
  }

  private Optional<GameSave> load(String fileName, boolean internal) {
    FileHandle fileHandle = internal ? files.internal(fileName) : files.local(fileName);
    if (fileHandle.exists()) {
      return Optional.of(myGson.fromJson(fileHandle.readString(), GameSave.class));
    }
    return Optional.absent();
  }
}
