package com.jingyuyao.tactical.data;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import com.google.common.base.Preconditions;
import com.google.common.collect.ListMultimap;
import com.jingyuyao.tactical.model.script.ActivateGroup;
import com.jingyuyao.tactical.model.script.Condition;
import com.jingyuyao.tactical.model.script.Dialogue;
import com.jingyuyao.tactical.model.script.Script;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.state.Turn;
import com.jingyuyao.tactical.model.world.Coordinate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class LevelLoader {

  private final DataConfig dataConfig;
  private final DataSerializer dataSerializer;
  private final Files files;
  private final DialogueLoader dialogueLoader;

  @Inject
  LevelLoader(
      DataConfig dataConfig,
      DataSerializer dataSerializer,
      Files files,
      DialogueLoader dialogueLoader) {
    this.dataConfig = dataConfig;
    this.dataSerializer = dataSerializer;
    this.files = files;
    this.dialogueLoader = dialogueLoader;
  }

  boolean hasLevel(int level) {
    FileHandle fileHandle = files.internal(dataConfig.getLevelDir(level));
    return fileHandle.exists();
  }

  /**
   * Create a new level save from the given {@code gameSave}.
   */
  LevelSave createNewSave(int level, GameSave gameSave) {
    Preconditions.checkArgument(hasLevel(level));
    LevelWorld levelWorld = load(dataConfig.getLevelWorldFileName(level), LevelWorld.class);
    Map<Coordinate, Ship> ships = new HashMap<>(levelWorld.getActiveShips());
    List<Coordinate> spawns = levelWorld.getPlayerSpawns();
    List<Ship> playerShips = gameSave.getPlayerShips();
    for (int i = 0; i < spawns.size() && i < playerShips.size(); i++) {
      ships.put(spawns.get(i), playerShips.get(i));
    }
    List<Ship> inactiveShips = new ArrayList<>(levelWorld.getInactiveShips());
    for (int i = spawns.size(); i < playerShips.size(); i++) {
      inactiveShips.add(playerShips.get(i));
    }
    return new LevelSave(ships, inactiveShips, new Turn(), loadScript(level));
  }

  private Script loadScript(int level) {
    LevelScript levelScript = load(dataConfig.getLevelScriptFileName(level), LevelScript.class);
    List<Condition> winConditions = levelScript.getWinConditions();
    List<Condition> loseConditions = levelScript.getLoseConditions();
    Map<Condition, ActivateGroup> groupActivations = levelScript.getGroupActivations();
    ListMultimap<Condition, Dialogue> dialogues = dialogueLoader.getDialogues(level);

    return new Script(winConditions, loseConditions, dialogues, groupActivations);
  }

  private <T> T load(String fileName, Class<T> clazz) {
    FileHandle fileHandle = files.internal(fileName);
    return dataSerializer.deserialize(fileHandle.reader(), clazz);
  }
}
