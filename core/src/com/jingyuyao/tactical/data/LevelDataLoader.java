package com.jingyuyao.tactical.data;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class LevelDataLoader {

  private final DataConfig dataConfig;
  private final MyGson myGson;
  private final Files files;

  @Inject
  LevelDataLoader(DataConfig dataConfig, MyGson myGson, Files files) {
    this.dataConfig = dataConfig;
    this.myGson = myGson;
    this.files = files;
  }

  boolean hasLevel(int level) {
    FileHandle fileHandle = files.internal(dataConfig.getLevelDir(level));
    return fileHandle.exists();
  }

  LevelWorld loadWorld(int level) {
    FileHandle fileHandle = files.internal(dataConfig.getLevelWorldFileName(level));
    return myGson.fromJson(fileHandle.readString(), LevelWorld.class);
  }

  LevelScript loadScript(int level) {
    FileHandle fileHandle = files.internal(dataConfig.getLevelScriptFileName(level));
    return myGson.fromJson(fileHandle.readString(), LevelScript.class);
  }
}
