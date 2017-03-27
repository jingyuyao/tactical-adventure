package com.jingyuyao.tactical;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.google.common.eventbus.EventBus;
import com.google.inject.Guice;
import com.jingyuyao.tactical.controller.ControllerModule;
import com.jingyuyao.tactical.data.DataModule;
import com.jingyuyao.tactical.data.GameSave;
import com.jingyuyao.tactical.data.GameSaveManager;
import com.jingyuyao.tactical.data.LevelData;
import com.jingyuyao.tactical.data.LevelDataManager;
import com.jingyuyao.tactical.data.LevelMapManager;
import com.jingyuyao.tactical.data.ModelManager;
import com.jingyuyao.tactical.model.ModelModule;
import com.jingyuyao.tactical.model.ModelModule.ModelEventBus;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.view.ViewModule;
import com.jingyuyao.tactical.view.WorldScreen;
import com.jingyuyao.tactical.view.WorldScreenSubscribers;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.inject.Inject;

public class TacticalAdventure extends Game {

  @Inject
  @ModelEventBus
  private EventBus modelEventBus;
  @Inject
  private GameSubscriber gameSubscriber;
  @Inject
  private WorldScreen worldScreen;
  @Inject
  private WorldScreenSubscribers worldScreenSubscribers;
  @Inject
  private ModelManager modelManager;
  @Inject
  private GameSaveManager gameSaveManager;
  @Inject
  private LevelDataManager levelDataManager;
  @Inject
  private LevelMapManager levelMapManager;
  @Inject
  private OrthogonalTiledMapRenderer tiledMapRenderer;
  @Inject
  private AssetManager assetManager;

  @Override
  public void create() {
    Guice
        .createInjector(
            new GameModule(this),
            new ModelModule(),
            new DataModule(),
            new ViewModule(),
            new ControllerModule())
        .injectMembers(this);

    modelEventBus.register(gameSubscriber);
    worldScreenSubscribers.register(modelEventBus);

    loadCurrentSave();
  }

  @Override
  public void pause() {
    super.pause();
  }

  @Override
  public void dispose() {
    super.dispose();
    worldScreen.dispose();
    assetManager.dispose();
  }

  void loadCurrentSave() {
    GameSave gameSave = gameSaveManager.load();
    int level = gameSave.getCurrentLevel();

    Map<Coordinate, Terrain> terrainMap = levelMapManager.load(level, tiledMapRenderer);

    if (!gameSave.isInProgress()) {
      loadLevel(gameSave, level);
    }

    modelManager.load(terrainMap, gameSave.getActivePlayers(), gameSave.getActiveEnemies());
    setScreen(worldScreen);
  }

  void loadLevel(GameSave gameSave, int level) {
    gameSave.clearActiveData();
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
    gameSaveManager.save(gameSave);
  }
}
