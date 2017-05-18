package com.jingyuyao.tactical.data;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Sets;
import com.jingyuyao.tactical.model.resource.ResourceKey;
import com.jingyuyao.tactical.model.script.Dialogue;
import com.jingyuyao.tactical.model.script.LevelTrigger;
import com.jingyuyao.tactical.model.script.Script;
import com.jingyuyao.tactical.model.script.ScriptActions;
import com.jingyuyao.tactical.model.state.Turn;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class ScriptLoader {

  private final DialogueLoader dialogueLoader;
  private final LevelDataLoader levelDataLoader;

  @Inject
  ScriptLoader(DialogueLoader dialogueLoader, LevelDataLoader levelDataLoader) {
    this.dialogueLoader = dialogueLoader;
    this.levelDataLoader = levelDataLoader;
  }

  Script load(int level) {
    Map<Turn, ScriptActions> turnScripts = loadTurnScripts(level);
    Map<ResourceKey, ScriptActions> deathScripts = loadDeathScripts();
    return new Script(turnScripts, deathScripts);
  }

  private Map<Turn, ScriptActions> loadTurnScripts(int level) {
    Map<Turn, ScriptActions> turnScripts = new HashMap<>();
    LevelScript levelScript = levelDataLoader.loadScript(level);
    Map<Turn, LevelTrigger> levelTriggers = levelScript.getLevelTriggers();
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
    ListMultimap<ResourceKey, Dialogue> deathDialogues = dialogueLoader.getDeathDialogues();
    for (ResourceKey name : deathDialogues.keySet()) {
      deathScripts.put(name, new ScriptActions(deathDialogues.get(name), LevelTrigger.NONE));
    }
    return deathScripts;
  }
}
