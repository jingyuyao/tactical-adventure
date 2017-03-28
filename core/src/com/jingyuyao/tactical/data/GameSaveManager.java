package com.jingyuyao.tactical.data;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GameSaveManager {

  private final DataConfig dataConfig;
  private final MyGson myGson;
  private final Files files;

  @Inject
  GameSaveManager(DataConfig dataConfig, MyGson myGson, Files files) {
    this.dataConfig = dataConfig;
    this.myGson = myGson;
    this.files = files;
  }

  public GameSave load() {
    FileHandle mainSave = getMainSaveHandle();
    if (mainSave.exists()) {
      return myGson.fromJson(mainSave.readString(), GameSave.class);
    }
    FileHandle startSave = files.local(dataConfig.getStartSaveFileName());
    if (startSave.exists()) {
      return myGson.fromJson(startSave.readString(), GameSave.class);
    }
    throw new IllegalStateException("Could not find either start or main game save file");
  }

  public void save(GameSave gameSave) {
    FileHandle mainSave = getMainSaveHandle();
    mainSave.writeString(myGson.toJson(gameSave), false);
  }

  private FileHandle getMainSaveHandle() {
    return files.local(dataConfig.getMainSaveFileName());
  }
}
