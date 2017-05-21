package com.jingyuyao.tactical.data;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import com.google.common.collect.ListMultimap;
import com.jingyuyao.tactical.model.script.Condition;
import com.jingyuyao.tactical.model.script.Dialogue;
import com.jingyuyao.tactical.model.script.Script;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.state.Turn;
import com.jingyuyao.tactical.model.world.Coordinate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class LevelLoader {

  private final DataConfig dataConfig;
  private final MyGson myGson;
  private final Files files;
  private final DialogueLoader dialogueLoader;

  @Inject
  LevelLoader(DataConfig dataConfig, MyGson myGson, Files files, DialogueLoader dialogueLoader) {
    this.dataConfig = dataConfig;
    this.myGson = myGson;
    this.files = files;
    this.dialogueLoader = dialogueLoader;
  }

  boolean hasLevel(int level) {
    FileHandle fileHandle = files.internal(dataConfig.getLevelDir(level));
    return fileHandle.exists();
  }

  /**
   * Create a new level save from the given {@code gameSave}. {@code gameSave} will be modified.
   */
  LevelSave createNewSave(int level, GameSave gameSave) {
    return new LevelSave(loadShips(level, gameSave), new Turn(), loadScript(level));
  }

  private Map<Coordinate, Ship> loadShips(int level, GameSave gameSave) {
    LevelWorld levelWorld = load(dataConfig.getLevelWorldFileName(level), LevelWorld.class);
    Map<Coordinate, Ship> ships = new HashMap<>();
    ships.putAll(gameSave.activateShips(levelWorld.getPlayerSpawns()));
    ships.putAll(levelWorld.getShips());
    return ships;
  }

  private Script loadScript(int level) {
    LevelScript levelScript = load(dataConfig.getLevelScriptFileName(level), LevelScript.class);
    List<Condition> winConditions = levelScript.getWinConditions();
    List<Condition> loseConditions = levelScript.getLoseConditions();
    ListMultimap<Condition, Dialogue> dialogues = dialogueLoader.getDialogues(level);

    return new Script(winConditions, loseConditions, dialogues);
  }

  private <T> T load(String fileName, Class<T> clazz) {
    FileHandle fileHandle = files.internal(fileName);
    return myGson.fromJson(fileHandle.readString(), clazz);
  }
}
