package com.jingyuyao.tactical.data;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class LevelInitLoader {

  private final DataConfig dataConfig;
  private final MyGson myGson;
  private final Files files;

  @Inject
  LevelInitLoader(DataConfig dataConfig, MyGson myGson, Files files) {
    this.dataConfig = dataConfig;
    this.myGson = myGson;
    this.files = files;
  }

  LevelInit load(int level) {
    FileHandle fileHandle = files.internal(dataConfig.getLevelInitFileName(level));
    if (fileHandle.exists()) {
      return myGson.fromJson(fileHandle.readString(), LevelInit.class);
    }
    throw new IllegalArgumentException("level " + level + " does not exist");
  }

  boolean hasLevel(int level) {
    FileHandle fileHandle = files.internal(dataConfig.getLevelInitFileName(level));
    return fileHandle.exists();
  }
}
