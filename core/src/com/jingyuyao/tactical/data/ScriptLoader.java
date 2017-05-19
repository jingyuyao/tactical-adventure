package com.jingyuyao.tactical.data;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.jingyuyao.tactical.model.resource.ResourceKey;
import com.jingyuyao.tactical.model.script.Condition;
import com.jingyuyao.tactical.model.script.Dialogue;
import com.jingyuyao.tactical.model.script.Script;
import com.jingyuyao.tactical.model.state.Turn;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class ScriptLoader {

  private final DialogueLoader dialogueLoader;
  private final LevelDataLoader levelDataLoader;
  private final GameDataManager gameDataManager;

  @Inject
  ScriptLoader(
      DialogueLoader dialogueLoader,
      LevelDataLoader levelDataLoader,
      GameDataManager gameDataManager) {
    this.dialogueLoader = dialogueLoader;
    this.levelDataLoader = levelDataLoader;
    this.gameDataManager = gameDataManager;
  }

  Script load(int level) {
    GameScript gameScript = gameDataManager.loadScript();
    LevelScript levelScript = levelDataLoader.loadScript(level);
    List<Condition> winConditions = levelScript.getWinConditions();
    List<Condition> loseConditions = new ArrayList<>();
    loseConditions.addAll(gameScript.getLoseConditions());
    loseConditions.addAll(levelScript.getLoseConditions());
    ListMultimap<Turn, Dialogue> levelDialogues = dialogueLoader.getLevelDialogues(level);
    ListMultimap<ResourceKey, Dialogue> deathDialogues = dialogueLoader.getDeathDialogues();
    // TODO: temp
    ListMultimap<Condition, Dialogue> dialogues = ArrayListMultimap.create();

    return new Script(winConditions, loseConditions, dialogues, levelDialogues, deathDialogues);
  }
}
