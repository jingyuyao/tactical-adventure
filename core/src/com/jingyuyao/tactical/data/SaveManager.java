package com.jingyuyao.tactical.data;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import com.google.common.base.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Manages loading, saving and deleting of save files. Treat this like a dumb I/O class.
 */
@Singleton
class SaveManager {

  private final DataConfig dataConfig;
  private final JsonSerializer jsonSerializer;
  private final Files files;

  @Inject
  SaveManager(DataConfig dataConfig, JsonSerializer jsonSerializer, Files files) {
    this.dataConfig = dataConfig;
    this.jsonSerializer = jsonSerializer;
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
      return Optional.of(jsonSerializer.deserialize(fileHandle.reader(), clazz));
    }
    return Optional.absent();
  }

  private void save(Object data, String fileName) {
    FileHandle fileHandle = files.local(fileName);
    jsonSerializer.serialize(data, fileHandle.writer(false));
  }

  private void remove(String fileName) {
    FileHandle fileHandle = files.local(fileName);
    if (fileHandle.exists()) {
      fileHandle.delete();
    }
  }
}
