package com.jingyuyao.tactical.data;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import com.google.common.base.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class LevelProgressManager {

  private final DataConfig dataConfig;
  private final MyGson myGson;
  private final Files files;

  @Inject
  LevelProgressManager(DataConfig dataConfig, MyGson myGson, Files files) {
    this.dataConfig = dataConfig;
    this.myGson = myGson;
    this.files = files;
  }

  Optional<LevelProgress> load() {
    FileHandle fileHandle = getFileHandle();
    if (fileHandle.exists()) {
      return Optional.of(myGson.fromJson(fileHandle.readString(), LevelProgress.class));
    }
    return Optional.absent();
  }

  void save(LevelProgress levelProgress) {
    FileHandle fileHandle = getFileHandle();
    fileHandle.writeString(myGson.toJson(levelProgress), false);
  }

  void removeSave() {
    FileHandle fileHandle = getFileHandle();
    if (fileHandle.exists()) {
      fileHandle.delete();
    }
  }

  private FileHandle getFileHandle() {
    return files.local(dataConfig.getMainLevelProgressFileName());
  }
}
