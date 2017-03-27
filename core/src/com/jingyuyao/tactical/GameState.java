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
import com.jingyuyao.tactical.model.world.World;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class GameState {

  // we don't keep a reference to GameState so we can sort of abuse Gson to copy data around
  // for us
  private final TacticalAdventure tacticalAdventure;
  private final GameSaveManager gameSaveManager;
  private final LevelDataManager levelDataManager;
  private final LevelMapManager levelMapManager;
  private final OrthogonalTiledMapRenderer tiledMapRenderer;
  private final Model model;
  private final World world;

  @Inject
  GameState(
      TacticalAdventure tacticalAdventure,
      GameSaveManager gameSaveManager,
      LevelDataManager levelDataManager,
      LevelMapManager levelMapManager,
      OrthogonalTiledMapRenderer tiledMapRenderer,
      Model model,
      World world) {
    this.tacticalAdventure = tacticalAdventure;
    this.gameSaveManager = gameSaveManager;
    this.levelDataManager = levelDataManager;
    this.levelMapManager = levelMapManager;
    this.tiledMapRenderer = tiledMapRenderer;
    this.model = model;
    this.world = world;
  }

  @Subscribe
  void levelComplete(LevelComplete levelComplete) {
    removeProgress();
    playCurrentLevel();
  }

  @Subscribe
  void levelFailed(LevelFailed levelFailed) {
    removeProgress();
    playCurrentLevel();
  }

  void playCurrentLevel() {
    GameSave gameSave = gameSaveManager.load();
    int level = gameSave.getCurrentLevel();

    if (!gameSave.isInProgress()) {
      loadLevel(gameSave, level);
    }

    Map<Coordinate, Terrain> terrainMap = levelMapManager.load(level, tiledMapRenderer);
    Map<Coordinate, Character> characterMap = extractCharacterMap(gameSave);

    model.initialize(terrainMap, characterMap);
    tacticalAdventure.goToWorldScreen();
  }

  void save() {
    model.prepForSave();
    GameSave gameSave = gameSaveManager.load();
    Map<Coordinate, Player> activePlayers = gameSave.getActivePlayers();
    Map<Coordinate, Enemy> activeEnemies = gameSave.getActiveEnemies();
    activePlayers.clear();
    activeEnemies.clear();
    activePlayers.putAll(world.getPlayers());
    activeEnemies.putAll(world.getEnemies());
    gameSaveManager.save(gameSave);
  }

  private void removeProgress() {
    model.reset();
    GameSave gameSave = gameSaveManager.load();
    gameSave.clearInProgressData();
    gameSaveManager.save(gameSave);
  }

  private Map<Coordinate, Character> extractCharacterMap(GameSave gameSave) {
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

  private void loadLevel(GameSave gameSave, int level) {
    gameSave.clearInProgressData();
    gameSave.setCurrentLevel(level);
    gameSave.setInProgress(true);

    LevelData levelData = levelDataManager.load(level);

    List<Player> startingPlayers = gameSave.getStartingPlayers();
    Map<Coordinate, Player> activePlayers = gameSave.getActivePlayers();
    List<Coordinate> playerSpawns = levelData.getPlayerSpawns();
    for (int i = 0; i < playerSpawns.size(); i++) {
      if (i >= startingPlayers.size()) {
        break;
      }
      activePlayers.put(playerSpawns.get(i), startingPlayers.get(i));
    }
    List<Player> inactivePlayers = gameSave.getInactivePlayers();
    for (int i = playerSpawns.size(); i < startingPlayers.size(); i++) {
      inactivePlayers.add(startingPlayers.get(i));
    }
    Map<Coordinate, Enemy> activeEnemies = gameSave.getActiveEnemies();
    for (Entry<Coordinate, Enemy> entry : levelData.getEnemies().entrySet()) {
      activeEnemies.put(entry.getKey(), entry.getValue());
    }
    gameSaveManager.save(gameSave);
  }
}
