package com.jingyuyao.tactical;

import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.data.GameSave;
import com.jingyuyao.tactical.data.GameSaveManager;
import com.jingyuyao.tactical.data.LevelDataManager;
import com.jingyuyao.tactical.data.LevelMapManager;
import com.jingyuyao.tactical.model.Model;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.event.LevelComplete;
import com.jingyuyao.tactical.model.event.LevelFailed;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.model.world.World;
import java.util.Map;
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
  private final World world;
  private final GameSave gameSave;

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
    this.gameSave = gameSaveManager.load();
  }

  @Subscribe
  void levelComplete(LevelComplete levelComplete) {
    model.reset();
    gameSave.setInProgress(false);
    continueLevel();
  }

  @Subscribe
  void levelFailed(LevelFailed levelFailed) {
    model.reset();
    gameSave.setInProgress(false);
    continueLevel();
  }

  void continueLevel() {
    int level = gameSave.getCurrentLevel();

    if (!gameSave.isInProgress()) {
      gameSaveManager.loadLevel(gameSave, levelDataManager.load(level));
    }

    Map<Coordinate, Terrain> terrainMap = levelMapManager.load(level, tiledMapRenderer);
    Map<Coordinate, Character> characterMap = gameSave.getActiveCharacters();

    model.initialize(terrainMap, characterMap);
    tacticalAdventure.goToWorldScreen();
  }

  void saveProgress() {
    model.prepForSave();
    gameSave.clearActiveCharacters();
    for (Cell cell : world.getCharacterSnapshot()) {
      Coordinate coordinate = cell.getCoordinate();
      if (cell.hasPlayer()) {
        gameSave.addActivePlayer(coordinate, cell.getPlayer());
      } else if (cell.hasEnemy()) {
        gameSave.addActiveEnemy(coordinate, cell.getEnemy());
      }
    }
    gameSaveManager.save(gameSave);
  }
}
