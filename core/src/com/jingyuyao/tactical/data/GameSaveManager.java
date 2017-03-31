package com.jingyuyao.tactical.data;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.world.Coordinate;
import java.util.List;
import java.util.Map.Entry;
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

  /**
   * Copies the {@link LevelData} into {@link GameSave}
   */
  public void loadLevel(GameSave gameSave, LevelData levelData) {
    gameSave.clearLevelProgress();
    gameSave.setCurrentLevel(levelData.getId());
    gameSave.setInProgress(true);

    List<Player> startingPlayers = gameSave.getStartingPlayers();
    List<Coordinate> playerSpawns = levelData.getPlayerSpawns();
    for (int i = 0; i < startingPlayers.size(); i++) {
      Player startingPlayer = startingPlayers.get(i);
      Player copy = myGson.deepCopy(startingPlayer, startingPlayer.getClass());
      if (i < playerSpawns.size()) {
        gameSave.addActivePlayer(playerSpawns.get(i), copy);
      } else {
        gameSave.addInactivePlayer(copy);
      }
    }

    for (Entry<Coordinate, Enemy> entry : levelData.getEnemies().entrySet()) {
      Enemy enemy = entry.getValue();
      gameSave.addActiveEnemy(entry.getKey(), myGson.deepCopy(enemy, enemy.getClass()));
    }
  }

  private FileHandle getMainSaveHandle() {
    return files.local(dataConfig.getMainSaveFileName());
  }
}
