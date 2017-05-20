package com.jingyuyao.tactical.data;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.jingyuyao.tactical.model.resource.ResourceKey;
import com.jingyuyao.tactical.model.script.Condition;
import com.jingyuyao.tactical.model.script.Dialogue;
import com.jingyuyao.tactical.model.script.Script;
import com.jingyuyao.tactical.model.state.Turn;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class ScriptLoader {

  private final DialogueLoader dialogueLoader;
  private final LevelDataLoader levelDataLoader;

  @Inject
  ScriptLoader(
      DialogueLoader dialogueLoader,
      LevelDataLoader levelDataLoader) {
    this.dialogueLoader = dialogueLoader;
    this.levelDataLoader = levelDataLoader;
  }

  Script load(int level) {
    LevelScript levelScript = levelDataLoader.loadScript(level);
    List<Condition> winConditions = levelScript.getWinConditions();
    List<Condition> loseConditions = levelScript.getLoseConditions();
    ListMultimap<Turn, Dialogue> levelDialogues = dialogueLoader.getLevelDialogues(level);
    ListMultimap<ResourceKey, Dialogue> deathDialogues = dialogueLoader.getDeathDialogues();
    // TODO: temp
    ListMultimap<Condition, Dialogue> dialogues = ArrayListMultimap.create();

    return new Script(winConditions, loseConditions, dialogues, levelDialogues, deathDialogues);
  }
}
