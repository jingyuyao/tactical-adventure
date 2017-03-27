package com.jingyuyao.tactical;

import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.google.common.collect.Sets;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.data.GameSave;
import com.jingyuyao.tactical.data.GameSaveManager;
import com.jingyuyao.tactical.data.LevelData;
import com.jingyuyao.tactical.data.LevelDataManager;
import com.jingyuyao.tactical.data.LevelMapManager;
import com.jingyuyao.tactical.model.Model;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.event.LevelComplete;
import com.jingyuyao.tactical.model.event.LevelFailed;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.model.world.Coordinate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class GameState {

  private final TacticalAdventure tacticalAdventure;
  private final GameSaveManager gameSaveManager;
  private final LevelDataManager levelDataManager;
  private final LevelMapManager levelMapManager;
  private final OrthogonalTiledMapRenderer tiledMapRenderer;
  private final Model model;
  private final GameSave gameSave;

  @Inject
  GameState(
      TacticalAdventure tacticalAdventure,
      GameSaveManager gameSaveManager,
      LevelDataManager levelDataManager,
      LevelMapManager levelMapManager,
      OrthogonalTiledMapRenderer tiledMapRenderer,
      Model model) {
    this.tacticalAdventure = tacticalAdventure;
    this.gameSaveManager = gameSaveManager;
    this.levelDataManager = levelDataManager;
    this.levelMapManager = levelMapManager;
    this.tiledMapRenderer = tiledMapRenderer;
    this.model = model;
    // TODO: some day we will support multiple game saves
    this.gameSave = gameSaveManager.load();
  }

  @Subscribe
  void levelComplete(LevelComplete levelComplete) {
    resetLevel();
    playCurrentLevel();
  }

  @Subscribe
  void levelFailed(LevelFailed levelFailed) {
    resetLevel();
    playCurrentLevel();
  }

  void playCurrentLevel() {
    int level = gameSave.getCurrentLevel();

    if (!gameSave.isInProgress()) {
      loadLevel(level);
    }

    Map<Coordinate, Terrain> terrainMap = levelMapManager.load(level, tiledMapRenderer);
    Map<Coordinate, Character> characterMap = makeCharacterMap();

    model.initialize(terrainMap, characterMap);
    tacticalAdventure.goToWorldScreen();
  }

  private void resetLevel() {
    model.reset();
    gameSave.clearInProgressData();
  }

  private Map<Coordinate, Character> makeCharacterMap() {
    Map<Coordinate, Character> characterMap = new HashMap<>();
    Map<Coordinate, Player> playerMap = gameSave.getActivePlayers();
    Map<Coordinate, Enemy> enemyMap = gameSave.getActiveEnemies();

    if (!Sets.intersection(playerMap.keySet(), enemyMap.keySet()).isEmpty()) {
      throw new IllegalArgumentException("Some player and enemy occupy the same coordinate");
    }

    characterMap.putAll(playerMap);
    characterMap.putAll(enemyMap);
    return characterMap;
  }

  private void loadLevel(int level) {
    gameSave.clearInProgressData();
    gameSave.setCurrentLevel(level);
    gameSave.setInProgress(true);

    LevelData levelData = levelDataManager.load(level);

    List<Player> startingPlayers = gameSave.getStartingPlayers();
    List<Coordinate> playerSpawns = levelData.getPlayerSpawns();
    for (int i = 0; i < playerSpawns.size(); i++) {
      if (i >= startingPlayers.size()) {
        break;
      }
      gameSave.addActive(playerSpawns.get(i), startingPlayers.get(i));
    }
    for (int i = playerSpawns.size(); i < startingPlayers.size(); i++) {
      gameSave.addInActive(startingPlayers.get(i));
    }
    for (Entry<Coordinate, Enemy> entry : levelData.getEnemies().entrySet()) {
      gameSave.addActive(entry.getKey(), entry.getValue());
    }
  }
}
