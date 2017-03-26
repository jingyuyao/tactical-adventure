package com.jingyuyao.tactical.data;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LevelDataManager {

  private final DataConfig dataConfig;
  private final MyGson myGson;
  private final Files files;

  @Inject
  LevelDataManager(DataConfig dataConfig, MyGson myGson, Files files) {
    this.dataConfig = dataConfig;
    this.myGson = myGson;
    this.files = files;
  }

  public LevelData load(int level) {
    FileHandle fileHandle = files.internal(dataConfig.getLevelDataFileName(level));
    if (fileHandle.exists()) {
      return myGson.fromJson(fileHandle.readString(), LevelData.class);
    }
    throw new IllegalArgumentException("level " + level + " does not exist");
  }
}
