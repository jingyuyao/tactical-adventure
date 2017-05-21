package com.jingyuyao.tactical.data;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import com.google.common.base.Optional;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Manages loading, saving and deleting of save files. Treat this like a dumb I/O class.
 */
@Singleton
class SaveManager {

  private final DataConfig dataConfig;
  private final MyGson myGson;
  private final Files files;

  @Inject
  SaveManager(DataConfig dataConfig, MyGson myGson, Files files) {
    this.dataConfig = dataConfig;
    this.myGson = myGson;
    this.files = files;
  }

  GameSave loadGameSave() {
    Optional<GameSave> main = loadLocal(dataConfig.getMainSaveFileName(), GameSave.class);
    if (main.isPresent()) {
      return main.get();
    }

    Optional<GameSave> start = loadInternal(dataConfig.getInitFileName(), GameSave.class);
    if (start.isPresent()) {
      GameSave startSave = start.get();
      save(startSave);
      return startSave;
    }

    throw new IllegalStateException("Could not find main init or save file!");
  }

  Optional<LevelSave> loadLevelSave() {
    return loadLocal(dataConfig.getMainLevelSaveFileName(), LevelSave.class);
  }

  void save(GameSave gameSave) {
    save(gameSave, dataConfig.getMainSaveFileName());
  }

  void save(LevelSave levelSave) {
    save(levelSave, dataConfig.getMainLevelSaveFileName());
  }

  void removeGameSave() {
    remove(dataConfig.getMainSaveFileName());
  }

  void removeLevelSave() {
    remove(dataConfig.getMainLevelSaveFileName());
  }

  private <T> Optional<T> loadLocal(String fileName, Class<T> clazz) {
    return load(fileName, clazz, false);
  }

  private <T> Optional<T> loadInternal(String fileName, Class<T> clazz) {
    return load(fileName, clazz, true);
  }

  private <T> Optional<T> load(String fileName, Class<T> clazz, boolean internal) {
    FileHandle fileHandle = internal ? files.internal(fileName) : files.local(fileName);
    if (fileHandle.exists()) {
      Reader reader = fileHandle.reader();
      T result = myGson.fromJson(reader, clazz);
      close(reader);
      return Optional.of(result);
    }
    return Optional.absent();
  }

  private void save(Object data, String fileName) {
    FileHandle fileHandle = files.local(fileName);
    Writer writer = fileHandle.writer(false);
    myGson.toJson(data, writer);
    close(writer);
  }

  private void remove(String fileName) {
    FileHandle fileHandle = files.local(fileName);
    if (fileHandle.exists()) {
      fileHandle.delete();
    }
  }

  private void close(Closeable closeable) {
    try {
      closeable.close();
    } catch (IOException e) {
      throw new RuntimeException("Unable to close a file");
    }
  }
}
