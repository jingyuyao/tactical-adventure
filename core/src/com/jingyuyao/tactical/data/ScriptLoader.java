package com.jingyuyao.tactical.data;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Sets;
import com.jingyuyao.tactical.model.resource.ResourceKey;
import com.jingyuyao.tactical.model.resource.ResourceKeyBundle;
import com.jingyuyao.tactical.model.script.Dialogue;
import com.jingyuyao.tactical.model.script.LevelTrigger;
import com.jingyuyao.tactical.model.script.Script;
import com.jingyuyao.tactical.model.script.ScriptActions;
import com.jingyuyao.tactical.model.state.Turn;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class ScriptLoader {

  private final DataConfig dataConfig;
  private final DialogueLoader dialogueLoader;
  private final LevelDataLoader levelDataLoader;
  private final GameDataManager gameDataManager;

  @Inject
  ScriptLoader(
      DataConfig dataConfig,
      DialogueLoader dialogueLoader,
      LevelDataLoader levelDataLoader,
      GameDataManager gameDataManager) {
    this.dataConfig = dataConfig;
    this.dialogueLoader = dialogueLoader;
    this.levelDataLoader = levelDataLoader;
    this.gameDataManager = gameDataManager;
  }

  Script load(int level) {
    Map<Turn, ScriptActions> turnScripts = loadTurnScripts(level);
    Map<ResourceKey, ScriptActions> deathScripts = loadDeathScripts();
    return new Script(turnScripts, deathScripts);
  }

  private Map<Turn, ScriptActions> loadTurnScripts(int level) {
    Map<Turn, ScriptActions> turnScripts = new HashMap<>();
    LevelScript levelScript = levelDataLoader.loadScript(level);
    Map<Turn, LevelTrigger> levelTriggers = levelScript.getTurnTriggers();
    ListMultimap<Turn, Dialogue> levelDialogues = dialogueLoader.getLevelDialogues(level);

    Set<Turn> actionTurns = Sets.union(levelTriggers.keySet(), levelDialogues.keySet());
    for (Turn turn : actionTurns) {
      List<Dialogue> dialogues = levelDialogues.get(turn);
      LevelTrigger levelTrigger =
          levelTriggers.containsKey(turn) ? levelTriggers.get(turn) : LevelTrigger.NONE;
      turnScripts.put(turn, new ScriptActions(dialogues, levelTrigger));
    }
    return turnScripts;
  }

  private Map<ResourceKey, ScriptActions> loadDeathScripts() {
    Map<ResourceKey, ScriptActions> deathScripts = new HashMap<>();
    GameScript gameScript = gameDataManager.loadScript();
    Set<ResourceKey> keepAlive = getKeepAlive(gameScript);
    ListMultimap<ResourceKey, Dialogue> deathDialogues = dialogueLoader.getDeathDialogues();

    Set<ResourceKey> actionNames = Sets.union(keepAlive, deathDialogues.keySet());
    for (ResourceKey name : actionNames) {
      List<Dialogue> deathDialogue = deathDialogues.get(name);
      LevelTrigger levelTrigger = keepAlive.contains(name) ? LevelTrigger.LOST : LevelTrigger.NONE;
      deathScripts.put(name, new ScriptActions(deathDialogue, levelTrigger));
    }
    return deathScripts;
  }

  private Set<ResourceKey> getKeepAlive(GameScript gameScript) {
    Set<ResourceKey> keepAlive = new HashSet<>();
    ResourceKeyBundle personNameBundle = dataConfig.getPersonNameBundle();
    for (String nameKey : gameScript.getKeepAlive()) {
      keepAlive.add(personNameBundle.get(nameKey));
    }
    return keepAlive;
  }
}
