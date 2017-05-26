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
  private final Files files;
  private final JsonLoader jsonLoader;
  private final KryoSerializer kryoSerializer;

  @Inject
  SaveManager(
      DataConfig dataConfig,
      Files files,
      JsonLoader jsonLoader,
      KryoSerializer kryoSerializer) {
    this.dataConfig = dataConfig;
    this.files = files;
    this.jsonLoader = jsonLoader;
    this.kryoSerializer = kryoSerializer;
  }

  GameSave loadGameSave() {
    Optional<GameSave> main = loadKryo(dataConfig.getMainSaveFileName(), GameSave.class);
    if (main.isPresent()) {
      return main.get();
    }

    Optional<GameSave> start = loadJson(dataConfig.getInitFileName(), GameSave.class);
    if (start.isPresent()) {
      GameSave startSave = start.get();
      save(startSave);
      return startSave;
    }

    throw new IllegalStateException("Could not find main init or save file!");
  }

  Optional<LevelSave> loadLevelSave() {
    return loadKryo(dataConfig.getMainLevelSaveFileName(), LevelSave.class);
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

  private <T> Optional<T> loadKryo(String fileName, Class<T> clazz) {
    FileHandle fileHandle = files.local(fileName);
    if (fileHandle.exists()) {
      return Optional.of(kryoSerializer.deserialize(fileHandle.read(), clazz));
    }
    return Optional.absent();
  }

  private <T> Optional<T> loadJson(String fileName, Class<T> clazz) {
    FileHandle fileHandle = files.internal(fileName);
    if (fileHandle.exists()) {
      return Optional.of(jsonLoader.deserialize(fileHandle.reader(), clazz));
    }
    return Optional.absent();
  }

  private void save(Object data, String fileName) {
    FileHandle fileHandle = files.local(fileName);
    kryoSerializer.serialize(data, fileHandle.write(false));
  }

  private void remove(String fileName) {
    FileHandle fileHandle = files.local(fileName);
    if (fileHandle.exists()) {
      fileHandle.delete();
    }
  }
}
